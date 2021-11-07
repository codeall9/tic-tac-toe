package io.codeall9.history

import io.codeall9.engine.initLocalGame
import io.codeall9.engine.model.*
import io.codeall9.engine.model.CellPosition.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class RecorderTest {

    @Test
    @DisplayName("The id in [GameLog] and [PlayedGame] are the same")
    fun allGameIdIsTheSame() = runBlockingTest {
        val ids = mutableListOf<GameId>()

        val newGame = recordingTicTacToe(
            initLocalGame,
            { gameLog -> ids.add(gameLog.gameId) },
            { playedGame -> ids.add(playedGame.gameId) },
        )

        val actual = listOf(TopEnd, CenterEnd, TopStart, BottomEnd, TopCenter)
            .fold(newGame(Player.O)) { acc, position -> acc.mark(position) }
            .run { ids.distinct() }
            .size

        assertEquals(1, actual, "expected one `gameId` after distinct")
    }

    @Test
    @DisplayName("Every new game has unique `gameId`")
    fun allGameIdAfterRestartIsNotEqual() = runBlockingTest {
        val list = mutableListOf<GameId>()

        val newGame = recordingTicTacToe(
            initLocalGame,
            { gameLog -> list.add(gameLog.gameId) },
            { /* no-op */ },
        )

        val moves = listOf(Center, TopCenter, BottomStart, TopStart, TopEnd)

        repeat(3) {
            moves.fold(newGame(Player.X)) { game, pos -> game.mark(pos) }
        }
        val actual = list.distinct().size

        assertEquals(3, actual, "expected contains 3 `gameId`")
    }

    @Test
    @DisplayName("Game can be replayed by `gameLogs`")
    fun replayedStateAreEqualToOriginal() = runBlockingTest {
        val moves = listOf(
            TopCenter,
            Center,
            TopEnd,
            TopStart,
            BottomEnd,
            CenterEnd,
            CenterStart,
            BottomCenter,
            BottomStart,
        )

        val gameLogs: MutableList<GameLog> = ArrayList(moves.size + 1)
        val newGame = recordingTicTacToe(
            initLocalGame,
            { gameLogs.add(it) },
            { /* no-op */ },
        )
        val original = moves.scan(newGame(Player.O)) { game, pos -> game.mark(pos) }

        val restart = gameLogs[0]
            .let { it as GameStarted }
            .let { initLocalGame(it.first) }
        val replayed = gameLogs
            .subList(1, gameLogs.size)
            .map { it as MovePlayed }
            .scan(restart) { acc, log -> acc.mark(log.position) }

        assertGameStateEquals(original, replayed)
    }

    @Test
    @DisplayName("Game result is recorded in [PlayedGame]")
    fun winnerIsEqualToOriginal() = runBlockingTest {
        var winner: Player? = null

        val newGame = recordingTicTacToe(
            initLocalGame,
            { /* no-op */ },
            { playedGame ->
                winner = playedGame
                    .let { it as PlayerWon }
                    .winner
            },
        )

        val original = listOf(Center, BottomEnd, BottomStart, TopEnd, CenterStart, CenterEnd)
            .fold(newGame(Player.O)) { game, pos -> game.mark(pos) }
            .let { it as GameWon }
            .winner

        assertEquals(original, winner)
    }

    @Test
    @DisplayName("`onResult` is invoked after match over")
    fun invokeTimeIsEqualTo2() = runBlockingTest {
        var count = 0

        val newGame = recordingTicTacToe(
            launchGame = initLocalGame,
            onResult = { count++ },
            onStateChanged = { /* no-op */ },
        )

        val moves = listOf(Center, CenterStart, BottomEnd, BottomStart, TopStart)

        val expected = 2

        repeat(expected) {
            moves.fold(newGame(Player.X)) { game, pos -> game.mark(pos) }
        }
        newGame(Player.O)

        assertEquals(expected, count)
    }

    private fun assertGameStateEquals(expected: List<GameState>, actual: List<GameState>) {
        assertEquals(expected.size, actual.size, "list size is not equal")
        expected.forEachIndexed { index, state ->
            assert(state.isContentEqual(actual[index])) { "index[$index] is not equal" }
        }
    }

    private fun GameState.isContentEqual(actual: GameState): Boolean {
        return when (this) {
            is PlayerOTurn -> isEqualTo(actual)
            is PlayerXTurn -> isEqualTo(actual)
            is GameTie -> isEqualTo(actual)
            is GameWon -> isEqualTo(actual)
        }
    }

    private fun PlayerOTurn.isEqualTo(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayerOTurn

        if (board != other.board) return false

        if (actions.keys.size != other.actions.keys.size) return false

        actions.all { it.key in other.actions }

        return true
    }

    private fun PlayerXTurn.isEqualTo(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayerXTurn

        if (board != other.board) return false

        if (actions.keys.size != other.actions.keys.size) return false

        actions.all { it.key in other.actions }

        return true
    }

    private fun GameWon.isEqualTo(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameWon

        if (board != other.board) return false

        if (winner != other.winner) return false

        return true
    }

    private fun GameTie.isEqualTo(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameTie

        if (board != other.board) return false

        return true
    }

    private fun Board.isEqualTo(other: Board): Boolean {
        return Board.AllPositions.all { get(it) == other[it] }
    }

    private suspend fun GameState.mark(position: CellPosition): GameState {
        return when (this) {
            is GameWon -> throw IllegalStateException("GameOver")
            is GameTie -> throw IllegalStateException("GameTie")
            is PlayerOTurn -> requireNotNull(actions[position], { "$position not found" }).invoke()
            is PlayerXTurn -> requireNotNull(actions[position], { "$position not found" }).invoke()
        }
    }
}