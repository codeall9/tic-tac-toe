package io.codeall9.core.history.util

import io.codeall9.core.history.event.GameStarted
import io.codeall9.core.history.event.GameTied
import io.codeall9.core.history.event.GameTransition
import io.codeall9.core.history.event.MovePlayed
import io.codeall9.core.history.event.PlayerWon
import io.codeall9.core.history.model.HistoryId
import io.codeall9.tictactoe.core.engine.model.Board
import io.codeall9.tictactoe.core.engine.model.Cell
import io.codeall9.tictactoe.core.engine.model.CellPosition
import io.codeall9.tictactoe.core.engine.model.GameState
import io.codeall9.tictactoe.core.engine.model.GameTie
import io.codeall9.tictactoe.core.engine.model.GameWon
import io.codeall9.tictactoe.core.engine.model.Player
import io.codeall9.tictactoe.core.engine.model.PlayerOTurn
import io.codeall9.tictactoe.core.engine.model.PlayerXTurn
import io.codeall9.tictactoe.core.engine.model.ValidRounds
import java.util.UUID

private val cells = listOf(Cell.O, Cell.X, Cell.Empty)
internal const val NUM_TESTS = 64
internal const val NUM_LARGE_TESTS = 16

internal fun randomBoard(randomCells: List<Cell> = List(9) { cells.random() }): Board {
    return Board(
        topStart = randomCells[0],
        topCenter = randomCells[1],
        topEnd = randomCells[2],
        centerStart = randomCells[3],
        center = randomCells[4],
        centerEnd = randomCells[5],
        bottomStart = randomCells[6],
        bottomCenter = randomCells[7],
        bottomEnd = randomCells[8],
    )
}

internal fun randomGameEvents(): List<GameTransition> {
    val historyId = HistoryId.of(UUID.randomUUID())
    // generate a random number of valid events
    val eventCount = (5..8).random()

    val events = buildList {
        var player = Player.X
        add(GameStarted(historyId, player))
        repeat(eventCount - 2) {
            val position = CellPosition.values().random()
            add(MovePlayed(historyId, player, position))
            player = if (player == Player.X) Player.O else Player.X
        }
        listOf(GameTied(historyId), PlayerWon(historyId, Player.X), PlayerWon(historyId, Player.O))
            .random()
            .let { add(it) }
    }

    return events
}

internal fun randomGameteState(round: Int = 1): GameState {
    val board = randomBoard()
    val endRound = (5..9).random()
    return if (round >= endRound) {
        listOf(
            GameWon(board, listOf(Player.O, Player.X).random()),
            GameTie(board)
        ).random()
    } else {
        listOf(
            PlayerOTurn(board, randomValidRounds(round + 1)),
            PlayerXTurn(board, randomValidRounds(round + 1)),
        ).random()
    }
}

internal fun randomValidRounds(round: Int = 1): ValidRounds {
    return mutableListOf(Cell.O, Cell.X, Cell.Empty, Cell.O, Cell.X, Cell.Empty, Cell.O, Cell.X, Cell.Empty)
        .apply { shuffle() }
        .let { randomBoard(it) }
        .asSequence()
        .filter { it.value == Cell.Empty }
        .map { it.key to suspend { randomGameteState(round) } }
        .associate { it }
}