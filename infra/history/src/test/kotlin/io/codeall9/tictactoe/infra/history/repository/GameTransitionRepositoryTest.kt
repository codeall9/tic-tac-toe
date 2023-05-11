package io.codeall9.tictactoe.infra.history.repository

import io.codeall9.core.history.event.GameTransition
import io.codeall9.core.history.model.HistoryId
import io.codeall9.history.test.NUM_TESTS
import io.codeall9.history.test.randomGameSequence
import io.codeall9.tictactoe.infra.history.comman.Transform
import io.codeall9.tictactoe.infra.history.database.GameEvent
import io.codeall9.tictactoe.infra.history.database.SimpleGameEventDao
import io.codeall9.tictactoe.infra.history.transform.toGameEvent
import io.codeall9.tictactoe.infra.history.transform.toGameTransition
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.assertAll
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@Timeout(10, unit = TimeUnit.SECONDS)
@TestMethodOrder(MethodOrderer.Random::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GameTransitionRepositoryTest {

    private val toGameTransition: Transform<GameEvent, GameTransition> = GameEvent::toGameTransition
    private val toGameEvent: Transform<GameTransition, GameEvent> = GameTransition::toGameEvent

    @RepeatedTest(NUM_TESTS)
    @DisplayName("Logged `GameTransition` should be equal to transformed GameEvent")
    fun logGameTransition() = runTest {
        val dao = SimpleGameEventDao()
        val logGameTransition = LogGameTransition(dao, ::println, toGameEvent)
        val expected = randomGameSequence()

        expected.forEach { logGameTransition(it) }

        assertContentEquals(expected, dao.raw.map(toGameTransition))
    }

    @RepeatedTest(NUM_TESTS)
    @DisplayName("Complete game sequence can be replayed from history")
    fun getGameHistory() = runTest {
        val dao = SimpleGameEventDao()
        val logGameTransition = LogGameTransition(dao, ::println, toGameEvent)
        val getGameHistory = GetGameHistory(dao, ::println, toGameTransition)

        randomGameSequence().forEach { logGameTransition(it) }

        val id = dao.raw.first()
            .historyId
            .let { UUID.fromString(it) }
            .let { HistoryId.of(it) }
        assertNotNull(getGameHistory(id))
    }

    @RepeatedTest(NUM_TESTS)
    @DisplayName("Retrieved game results should match most recent data")
    fun getRecentGameResult() = runTest {
        val dao = SimpleGameEventDao()
        val logGameTransition = LogGameTransition(dao, ::println, toGameEvent)
        val getRecentGameResult = GetRecentGameResult(dao, ::println, toGameTransition)
        val allGameResults = mutableListOf<GameTransition>()
        val total = 10

        repeat((0..13).random()) {
            randomGameSequence()
                .also { allGameResults.add(it.last()) }
                .forEach { logGameTransition(it) }
        }

        val actual = getRecentGameResult(total.toUInt()).first()

        val expected = allGameResults.takeLast(total)

        assertAll(
            { assertTrue(actual.size <= total, "The list size should less than the query, list: ${actual.size}, query: $total") },
            { assertTrue(expected.containsAll(actual), "The results should contains expected data, expected: $expected, actual: $actual") },
        )
    }

    @Test
    @DisplayName("Any failure from DAO should be map to null or empty list")
    fun illegalDao() = runTest {
        val dao = SimpleGameEventDao()
        val logGameTransition = LogGameTransition(dao, ::println, toGameEvent)
        val getGameHistory = GetGameHistory(dao, ::println, toGameTransition)
        val getRecentGameResult = GetRecentGameResult(dao, ::println, toGameTransition)

        randomGameSequence().forEach { logGameTransition(it) }
        dao.isAccessDenied = true

        val id = dao.raw.first()
            .historyId
            .let { UUID.fromString(it) }
            .let { HistoryId.of(it) }
        dao.isAccessDenied = true
        assertNull(getGameHistory(id))
        assertTrue(getRecentGameResult(10U).first().isEmpty())
    }

    @Test
    @DisplayName("Illegal results should be filtered")
    fun illegalGameTransition() = runTest {
        val dao = SimpleGameEventDao()
        val throwOnce = AtomicBoolean()
        val toIllegalGameTransition: Transform<GameEvent, GameTransition> = {
            if (eventType != GameEvent.EventType.ENDED || throwOnce.get()) {
                this.toGameTransition()
            } else {
                throwOnce.set(true)
                error("random error")
            }
        }
        val logGameTransition = LogGameTransition(dao, ::println, toGameEvent)
        val getRecentGameResult = GetRecentGameResult(dao, ::println, toIllegalGameTransition)
        val total = 3

        repeat(total) {
            randomGameSequence().forEach { logGameTransition(it) }
        }
        assertEquals(total - 1, getRecentGameResult(total.toUInt()).first().size)
    }
}