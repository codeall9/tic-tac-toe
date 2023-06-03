package io.codeall9.core.history.model

import io.codeall9.core.history.event.GameStarted
import io.codeall9.core.history.event.MovePlayed
import io.codeall9.core.history.event.PlayerWon
import io.codeall9.history.test.NUM_TESTS
import io.codeall9.history.test.randomBoard
import io.codeall9.history.test.randomGameSequence
import io.codeall9.tictactoe.core.engine.model.CellPosition
import io.codeall9.tictactoe.core.engine.model.Player
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
internal class GameHistoryTest {

    @RepeatedTest(NUM_TESTS)
    @DisplayName("replay() only contains valid game events")
    fun replayGameTransitions() {
        val events = randomGameSequence()
        val gameHistory = GameHistory.replayOrNull(events)

        assertNotNull(gameHistory, "replayOrNull should not return null, input: $events")
        assertEquals(events.size - 1, gameHistory.totalRounds)
    }

    @Test
    @DisplayName("replay() throw Exception if game events is invalid")
    fun replayInvalidEvents() {
        val id = HistoryId.of(UUID.randomUUID())
        assertAll(
            "invalid events can't be replay()",
            {
                assertThrows<IllegalArgumentException> { GameHistory.replay(emptyList()) }
            },
            {
                val events = listOf(
                    GameStarted(id, Player.O),
                    MovePlayed(id, Player.O, CellPosition.TopCenter),
                    GameStarted(id, Player.X),
                    MovePlayed(id, Player.X, CellPosition.CenterEnd),
                )
                assertThrows<IllegalArgumentException> { GameHistory.replay(events) }
            },
            {
                val events = listOf(
                    GameStarted(id, Player.O),
                    MovePlayed(id, Player.O, CellPosition.TopCenter),
                    GameStarted(id, Player.X),
                    PlayerWon(id, Player.X),
                    MovePlayed(id, Player.O, CellPosition.BottomCenter),
                )
                assertThrows<IllegalArgumentException> { GameHistory.replay(events) }
            },
        )
    }

    @RepeatedTest(NUM_TESTS)
    @DisplayName("get() should return the correct board for a valid round number")
    fun gameBoard() {
        val numRounds = Random.nextInt(6, 10)
        val snapshots = List(numRounds) { lazyOf(randomBoard()) }
        val gameHistory = GameHistory(snapshots)

        for (round in 1..numRounds) {
            assertTrue("board is not correct") {
                snapshots[round - 1].value.isAllPositionEquals(gameHistory[round])
            }
        }
    }
}