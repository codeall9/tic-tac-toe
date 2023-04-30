package io.codeall9.core.history

import io.codeall9.core.history.event.GameTransition
import io.codeall9.core.history.util.NUM_LARGE_TESTS
import io.codeall9.core.history.util.NUM_TESTS
import io.codeall9.core.history.util.initFakeGame
import io.codeall9.tictactoe.core.engine.LaunchTicTacToe
import io.codeall9.tictactoe.core.engine.model.GameState
import io.codeall9.tictactoe.core.engine.model.GameTie
import io.codeall9.tictactoe.core.engine.model.GameWon
import io.codeall9.tictactoe.core.engine.model.Player
import io.codeall9.tictactoe.core.engine.model.PlayerOTurn
import io.codeall9.tictactoe.core.engine.model.PlayerXTurn
import io.codeall9.tictactoe.core.engine.model.isOnGoing
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Timeout
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

@Timeout(value = 5, unit = TimeUnit.SECONDS)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class RecordGameTransitionTest {

    @RepeatedTest(NUM_LARGE_TESTS)
    @DisplayName("Every new game has unique `HistoryId`")
    fun recordGamePlays() = runTest {
        val recorded = mutableListOf<GameTransition>()
        val launcher = recordGameTransition(
            launchGame = ::initFakeGame,
            onStateChanged = { recorded.add(it) },
        )
        val expected = (1..9).random()

        repeat(expected) {
            playGameRandomly(launcher)
        }

        val actual = recorded.distinctBy { it.historyId }.count()

        assertEquals(expected, actual, "expected $expected `historyId` of game events")
    }

    @RepeatedTest(NUM_TESTS)
    @DisplayName("Every completed game should contain N + 1 events")
    fun recordGameTransition() = runTest {
        val recorded = mutableListOf<GameTransition>()
        val launcher = recordGameTransition(
            launchGame = ::initFakeGame,
            onStateChanged = { recorded.add(it) },
        )

        val allStates = playGameRandomly(launcher)

        assertEquals(allStates.size + 1, recorded.size)
    }

    private suspend fun playGameRandomly(newGame: LaunchTicTacToe): List<GameState> = buildList<GameState> {
        var game = newGame(listOf(Player.X, Player.O).random())
            .also { add(it) }
        while (game.isOnGoing()) {
            val randomRounds = when (game) {
                is GameTie -> error("game is ended")
                is GameWon -> error("game is ended")
                is PlayerOTurn -> game.actions
                is PlayerXTurn -> game.actions
            }
            game = randomRounds.asSequence()
                .shuffled()
                .first()
                .value
                .let { mark -> mark() }
                .also { add(it) }
        }
    }
}