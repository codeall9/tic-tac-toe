package io.codeall9.tictactoe.infra.history.transform

import io.codeall9.core.history.event.GameStarted
import io.codeall9.core.history.event.GameTied
import io.codeall9.core.history.event.GameTransition
import io.codeall9.core.history.event.MovePlayed
import io.codeall9.core.history.event.PlayerWon
import io.codeall9.core.history.model.HistoryId
import io.codeall9.history.test.NUM_TESTS
import io.codeall9.tictactoe.core.engine.model.CellPosition
import io.codeall9.tictactoe.core.engine.model.Player
import io.codeall9.tictactoe.infra.history.comman.Transform
import io.codeall9.tictactoe.infra.history.database.GameEvent
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import java.util.UUID
import kotlin.test.assertEquals

@TestMethodOrder(MethodOrderer.Random::class)
@DisplayName("GameEvent and GameTransition Conversion Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GameEventConversionTest {

    val toGameTransition: Transform<GameEvent, GameTransition> = GameEvent::toGameTransition
    val toGameEvent: Transform<GameTransition, GameEvent> = GameTransition::toGameEvent

    @RepeatedTest(NUM_TESTS)
    @DisplayName("Converted GameTransition should be equal to original GameEvent")
    fun gameEventToEntity() {
        val player = Player.values().random()
        val historyId = UUID.randomUUID().toString()
        val position = CellPosition.values().random()
        val event1 = GameEvent(
            GameEvent.EventType.STARTED,
            historyId,
            player,
            null,
        )
        val event2 = GameEvent(
            GameEvent.EventType.MOVE_PLAYED,
            historyId,
            player,
            position,
        )
        val event3 = GameEvent(
            GameEvent.EventType.ENDED,
            historyId,
            player,
            null,
        )
        val event4 = GameEvent(
            GameEvent.EventType.ENDED,
            historyId,
            null,
            null,
        )

        val transition1 = event1.toGameTransition()
        val transition2 = event2.toGameTransition()
        val transition3 = event3.toGameTransition()
        val transition4 = event4.toGameTransition()

        assertEquals(event1, transition1.toGameEvent().copy(createdAt = event1.createdAt))
        assertEquals(event2, transition2.toGameEvent().copy(createdAt = event2.createdAt))
        assertEquals(event3, transition3.toGameEvent().copy(createdAt = event3.createdAt))
        assertEquals(event4, transition4.toGameEvent().copy(createdAt = event4.createdAt))
    }

    @RepeatedTest(NUM_TESTS)
    @DisplayName("Converted GameEvent should be equal to original GameTransition")
    fun gameTransitionToDto() {
        val player = Player.values().random()
        val historyId = HistoryId.of(UUID.randomUUID())
        val position = CellPosition.values().random()

        val transition1 = GameStarted(id = historyId, first = player)
        val transition2 = MovePlayed(id = historyId, player = player, position = position)
        val transition3 = GameTied(id = historyId)
        val transition4 = PlayerWon(id = historyId, winner = player)

        val event1 = transition1.toGameEvent()
        val event2 = transition2.toGameEvent()
        val event3 = transition3.toGameEvent()
        val event4 = transition4.toGameEvent()

        assertEquals(transition1, event1.toGameTransition())
        assertEquals(transition2, event2.toGameTransition())
        assertEquals(transition3, event3.toGameTransition())
        assertEquals(transition4, event4.toGameTransition())
    }

    @Test
    @DisplayName("Converted GameTransition should always valid")
    fun invalidGameEvent() {
        val historyId = UUID.randomUUID().toString()
        val invalidGameStarted = GameEvent(
            GameEvent.EventType.STARTED,
            historyId,
            null,
            null,
        )
        val invalidMovePlayed = GameEvent(
            GameEvent.EventType.MOVE_PLAYED,
            historyId,
            Player.X,
            null,
        )
        val invalidId = GameEvent(
            GameEvent.EventType.ENDED,
            "invalid_id",
            null,
            null,
        )

        assertAll(
            "invalid GameEvent can't be converted",
            { assertThrows<IllegalStateException> { invalidGameStarted.toGameTransition() } },
            { assertThrows<IllegalStateException> { invalidMovePlayed.toGameTransition() } },
            { assertThrows<IllegalStateException> { invalidId.toGameTransition() } },
        )
    }
}