package io.codeall9.tictactoe.core.engine

import io.codeall9.tictactoe.core.engine.model.Board
import io.codeall9.tictactoe.core.engine.model.Cell
import io.codeall9.tictactoe.core.engine.model.CellPosition
import io.codeall9.tictactoe.core.engine.model.CellPosition.BottomCenter
import io.codeall9.tictactoe.core.engine.model.CellPosition.BottomEnd
import io.codeall9.tictactoe.core.engine.model.CellPosition.BottomStart
import io.codeall9.tictactoe.core.engine.model.CellPosition.Center
import io.codeall9.tictactoe.core.engine.model.CellPosition.CenterEnd
import io.codeall9.tictactoe.core.engine.model.CellPosition.CenterStart
import io.codeall9.tictactoe.core.engine.model.CellPosition.TopCenter
import io.codeall9.tictactoe.core.engine.model.CellPosition.TopEnd
import io.codeall9.tictactoe.core.engine.model.CellPosition.TopStart
import io.codeall9.tictactoe.core.engine.model.GameState
import io.codeall9.tictactoe.core.engine.model.GameTie
import io.codeall9.tictactoe.core.engine.model.GameWon
import io.codeall9.tictactoe.core.engine.model.Player
import io.codeall9.tictactoe.core.engine.model.PlayerOTurn
import io.codeall9.tictactoe.core.engine.model.PlayerXTurn
import io.codeall9.tictactoe.core.engine.model.ValidRounds
import java.util.concurrent.atomic.AtomicBoolean


private val Player.opponent : Player get() = if (this == Player.O) Player.X else Player.O

internal fun initGame(first: Player = Player.O): GameState {
    val initState = HashMap<CellPosition, Cell>(9)
        .also {
            Board.AllPositions.associateWithTo(it) { Cell.Empty }
        }
    val emptyBoard = Board(initState)
    return first
        .getValidRounds(initState, emptyBoard) { position ->
            initState[position] = Cell.Marked(this)
        }
        .let(first.turnWith(emptyBoard))
}

private fun Player.turnWith(board: Board): (ValidRounds) -> GameState = { actions ->
    if (this == Player.O) {
        PlayerOTurn(board, actions)
    } else {
        PlayerXTurn(board, actions)
    }
}

private fun Player.getValidRounds(
    cells: Map<CellPosition, Cell>,
    board: Board,
    markCell: Player.(CellPosition) -> Unit,
): ValidRounds {
    val nextPlayer = opponent
    val isPlayed = AtomicBoolean(false)
    return cells
        .filterValues { it is Cell.Empty }
        .mapValues { (position, _) ->
            requireInvoke(
                block = generateValidMove(cells, board, position, nextPlayer, markCell),
                lazyMessage = { "player $name already played!" },
                predicate = { !isPlayed.getAndSet(true) }
            )
        }
}

private fun Player.generateValidMove(
    cells: Map<CellPosition, Cell>,
    board: Board,
    position: CellPosition,
    nextPlayer: Player,
    markCell: Player.(CellPosition) -> Unit,
): suspend () -> GameState {
    return escaping@{
        markCell(position)
        val winner = board.findWinnerOrNull()
        if (winner != null) {
            return@escaping GameWon(board, winner)
        }
        if (board.isAllMarked()) {
            return@escaping GameTie(board)
        }
        return@escaping nextPlayer
            .getValidRounds(cells, board, markCell)
            .let(nextPlayer.turnWith(board))
    }
}

private fun Board.findWinnerOrNull(): Player? {
    return findDiagonallyRowWinner()
        ?: findHorizontallyRowWinner()
        ?: findVerticallyRowWinner()
}

private fun Board.findHorizontallyRowWinner(): Player? {
    val marked1 = this[TopStart].let { it as? Cell.Marked }
    if ((marked1 != null) && (marked1 == this[TopCenter]) && (marked1 == this[TopEnd])) {
        return marked1.player
    }

    val marked2 = this[CenterStart].let { it as? Cell.Marked }
    if ((marked2 != null) && (marked2 == this[Center]) && (marked2 == this[CenterEnd])) {
        return marked2.player
    }

    val marked3 = this[BottomStart].let { it as? Cell.Marked }
    if ((marked3 != null) && (marked3 == this[BottomCenter]) && (marked3 == this[BottomEnd])) {
        return marked3.player
    }

    return null
}

private fun Board.findVerticallyRowWinner(): Player? {
    val marked1 = this[TopStart].let { it as? Cell.Marked }
    if ((marked1 != null) && (marked1 == this[CenterStart]) && (marked1 == this[BottomStart])) {
        return marked1.player
    }

    val marked2 = this[TopCenter].let { it as? Cell.Marked }
    if ((marked2 != null) && (marked2 == this[Center]) && (marked2 == this[BottomCenter])) {
        return marked2.player
    }

    val marked3 = this[TopEnd].let { it as? Cell.Marked }
    if ((marked3 != null) && (marked3 == this[CenterEnd]) && (marked3 == this[BottomEnd])) {
        return marked3.player
    }

    return null
}

private fun Board.findDiagonallyRowWinner(): Player? {
    val marked1 = this[TopStart].let { it as? Cell.Marked }
    if ((marked1 != null) && (marked1 == this[Center]) && (marked1 == this[BottomEnd])) {
        return marked1.player
    }

    val marked2 = this[TopEnd].let { it as? Cell.Marked }
    if ((marked2 != null) && (marked2 == this[Center]) && (marked2 == this[BottomStart])) {
        return marked2.player
    }

    return null
}