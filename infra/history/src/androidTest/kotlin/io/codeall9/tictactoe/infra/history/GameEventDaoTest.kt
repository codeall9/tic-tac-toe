package io.codeall9.tictactoe.infra.history

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.codeall9.core.history.event.GameTransition
import io.codeall9.history.test.randomGameSequence
import io.codeall9.history.test.randomInstant
import io.codeall9.tictactoe.core.engine.model.CellPosition
import io.codeall9.tictactoe.core.engine.model.Player
import io.codeall9.tictactoe.infra.history.comman.Transform
import io.codeall9.tictactoe.infra.history.database.GameEvent
import io.codeall9.tictactoe.infra.history.database.GameEventDao
import io.codeall9.tictactoe.infra.history.database.HistoryDatabase
import io.codeall9.tictactoe.infra.history.factory.initInMemoryDb
import io.codeall9.tictactoe.infra.history.transform.toGameEvent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.random.Random
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@RunWith(AndroidJUnit4::class)
internal class GameEventDaoTest {

    private lateinit var dao: GameEventDao
    private lateinit var db: HistoryDatabase
    private lateinit var toRoomEntity: Transform<GameTransition, GameEvent>

    @Before
    fun setupInfra() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = initInMemoryDb(context)
        dao = db.gameEventDao()
        toRoomEntity = GameTransition::toGameEvent
    }

    @After
    @Throws(IOException::class)
    fun closeInfra() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetGameEvent() {
        val event = GameEvent(
            eventType = GameEvent.EventType.MOVE_PLAYED,
            historyId = "historyId",
            player = Player.O,
            position = CellPosition.BottomEnd,
        )
        runTest {
            val eventId = dao.insert(event)
            val actual = dao.getGameEvent(eventId)

            assertNotEquals(0, actual.eventId, "event id should be auto generated")
            assertEquals(event.copy(eventId = eventId), actual)
        }
    }

    @Test
    @Throws(Exception::class)
    fun queryGameHistory() {
        val events = randomGameSequence()
            .map { event ->
                event.toRoomEntity()
                    .copy(
                        eventId = Random.nextLong(from = 0, until = Long.MAX_VALUE),
                        createdAt = randomInstant()
                    )
            }
        val historyId = events.first().historyId
        val expected = events.sortedBy { it.createdAt.plusNanos(it.eventId) }

        runTest {
            events.forEach { dao.insert(it) }

            val actual = dao.getGameHistory(historyId)

            assertContentEquals(expected, actual)
        }
    }

    @Test
    @Throws(Exception::class)
    fun queryRecentGameResult() {
        val total = (1..5).random()
        val events = (1..total).asSequence()
            .flatMap { randomGameSequence() }
            .map { event ->
                event.toRoomEntity()
                    .copy(
                        eventId = Random.nextLong(from = 0, until = Long.MAX_VALUE),
                        createdAt = randomInstant()
                    )
            }
            .toList()
        val requestCount = (1..12).random()
        val expected = events.asSequence()
            .filter { it.eventType == GameEvent.EventType.ENDED }
            .sortedByDescending { it.createdAt.plusNanos(it.eventId) }
            .take(requestCount)
            .toList()


        runTest {
            events.forEach { dao.insert(it) }

            val actual = dao.getRecentResults(requestCount).first()

            assertContentEquals(expected, actual)
        }
    }

    @Test
    @Throws(Exception::class)
    fun queryGameResultById() {
        val total = (1..5).random()
        val events = (1..total).asSequence()
            .flatMap { randomGameSequence() }
            .map(toRoomEntity)
            .toList()
        val historyId = events.random().historyId
        val expected = events.asSequence()
            .filter { it.eventType == GameEvent.EventType.ENDED }
            .first { it.historyId == historyId }

        runTest {
            events.forEach { dao.insert(it) }

            val actual = dao.getGameResult(historyId).first()

            assertEquals(expected.copy(eventId = actual.eventId), actual)
        }
    }
}