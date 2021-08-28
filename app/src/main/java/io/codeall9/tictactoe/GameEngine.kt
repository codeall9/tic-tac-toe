package io.codeall9.tictactoe

import io.codeall9.tictactoe.model.*
import java.util.concurrent.atomic.AtomicBoolean

typealias TicTacToeInitializer = suspend (Player) -> MatchResult

private val Player.opponent : Player get() = if (this == Player.O) Player.X else Player.O

val initLocalGame: TicTacToeInitializer = { initGame(it) }

private fun initGame(first: Player = Player.O): MatchResult {
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

private fun Player.turnWith(board: Board): (ValidRounds) -> MatchResult = { actions ->
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
    val isPlayed = AtomicBoolean(false)
    val nextPlayer = opponent
    return cells
        .filterValues { it is Cell.Empty }
        .mapValues { (position, _) ->
            requireInvoke(
                block = suspend escaping@{
                    markCell(position)
                    val winner = board.findWinnerOrNull()
                    if (winner != null) {
                        return@escaping GameOver(board, winner)
                    }
                    if (board.isAllMarked()) {
                        return@escaping GameTie(board)
                    }
                    return@escaping nextPlayer
                        .getValidRounds(cells, board, markCell)
                        .let(nextPlayer.turnWith(board))
                },
                lazyMessage = { "player $name already played!" },
                predicate = { !isPlayed.getAndSet(true) }
            )
        }
}

private fun Board.findWinnerOrNull(): Player? {
    return findDiagonallyRowWinner()
        ?: findHorizontallyRowWinner()
        ?: findVerticallyRowWinner()
}

private fun Board.findHorizontallyRowWinner(): Player? {
    val marked1 = topStart.let { it as? Cell.Marked }
    if ((marked1 != null) && (marked1 == topCenter) && (marked1 == topEnd)) {
        return marked1.player
    }

    val marked2 = centerStart.let { it as? Cell.Marked }
    if ((marked2 != null) && (marked2 == center) && (marked2 == centerEnd)) {
        return marked2.player
    }

    val marked3 = bottomStart.let { it as? Cell.Marked }
    if ((marked3 != null) && (marked3 == bottomCenter) && (marked3 == bottomEnd)) {
        return marked3.player
    }

    return null
}

private fun Board.findVerticallyRowWinner(): Player? {
    val marked1 = topStart.let { it as? Cell.Marked }
    if ((marked1 != null) && (marked1 == centerStart) && (marked1 == bottomStart)) {
        return marked1.player
    }

    val marked2 = topCenter.let { it as? Cell.Marked }
    if ((marked2 != null) && (marked2 == center) && (marked2 == bottomCenter)) {
        return marked2.player
    }

    val marked3 = topEnd.let { it as? Cell.Marked }
    if ((marked3 != null) && (marked3 == centerEnd) && (marked3 == bottomEnd)) {
        return marked3.player
    }

    return null
}

private fun Board.findDiagonallyRowWinner(): Player? {
    val marked1 = topStart.let { it as? Cell.Marked }
    if ((marked1 != null) && (marked1 == center) && (marked1 == bottomEnd)) {
        return marked1.player
    }

    val marked2 = topEnd.let { it as? Cell.Marked }
    if ((marked2 != null) && (marked2 == center) && (marked2 == bottomStart)) {
        return marked2.player
    }

    return null
}