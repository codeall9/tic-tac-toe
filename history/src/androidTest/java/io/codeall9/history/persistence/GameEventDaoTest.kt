package io.codeall9.history.persistence

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.codeall9.engine.model.CellPosition.*
import io.codeall9.engine.model.Player
import io.codeall9.history.persistence.GameEvent.EventType.MOVE_PLAYED
import io.codeall9.history.persistence.GameEvent.EventType.STARTED
import io.codeall9.history.room.InstantConverter
import io.codeall9.history.services.buildInMemoryDb
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import java.io.IOException
import java.time.Instant
import java.util.*
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
internal class GameEventDaoTest {

    private lateinit var db: HistoryDatabase
    private lateinit var dao: GameEventDao
    private val testScope = TestScope()
    private val testDispatcher = StandardTestDispatcher(testScope.testScheduler, name = "IO dispatcher")

    @BeforeTest
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = buildInMemoryDb(context) {
            setQueryExecutor(testDispatcher.asExecutor())
            addTypeConverter(InstantConverter())
        }
        dao = db.gameEventDao()
    }

    @AfterTest
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
        testScope.cancel()
    }

    @Test
    fun queriedEventIsEqualToOriginal() = testScope.runTest {
        val original = GameEvent("any", MOVE_PLAYED, Player.O, Center, 90)

        dao.insert(original)
        val actual = dao.getGameEvent(original.eventId)

        assertEquals(original, actual)
    }

    @Test
    fun autoGeneratedIdIsUnique() = testScope.runTest  {
        val id = UUID.randomUUID().toString()

        val generatedIds = dao.insert(
            GameEvent(id, STARTED, Player.O, null, 0),
            GameEvent(id, MOVE_PLAYED, Player.O, Center, 4),
            GameEvent(id, MOVE_PLAYED, Player.X, BottomEnd),
            GameEvent(id, MOVE_PLAYED, Player.O, TopStart),
        )

        assertThat(generatedIds).containsNoDuplicates()
    }

    @Test
    fun filterSecondIdIsSameAsDoNothing() = testScope.runTest {
        val firstId = UUID.randomUUID().toString()
        val secondId = UUID.randomUUID().toString()

        dao.insert(
            GameEvent(firstId, STARTED, Player.O, null, 1),
            GameEvent(firstId, MOVE_PLAYED, Player.O, Center, 2),
            GameEvent(firstId, MOVE_PLAYED, Player.X, BottomEnd, 3),
            GameEvent(secondId, STARTED, Player.X, null, 10),
            GameEvent(secondId, MOVE_PLAYED, Player.X, TopEnd, 11),
            GameEvent(secondId, MOVE_PLAYED, Player.O, BottomCenter, 12),
        )

        val actual = dao.getGameHistory(secondId)
        val filtered = actual.filter { it.gameId == secondId }

        assertThat(actual)
        assertEquals(filtered, actual)
    }

    @Test
    fun historyIsSortedByTime() = testScope.runTest {
        val id1 = UUID.randomUUID().toString()
        val now = Instant.now()
        val comparator = Comparator<GameEvent> { first, second ->
            first.createdAt.compareTo(second.createdAt)
        }

        dao.insert(
            GameEvent(4, id1, MOVE_PLAYED, Player.O, Center, now.plusSeconds(2)),
            GameEvent(7, id1, MOVE_PLAYED, Player.X, CenterEnd, now.plusSeconds(5)),
            GameEvent(3, id1, STARTED, Player.O, null, now.plusSeconds(1)),
            GameEvent(6, id1, MOVE_PLAYED, Player.O, TopStart, now.plusSeconds(4)),
            GameEvent(5, id1, MOVE_PLAYED, Player.X, TopEnd, now.plusSeconds(3)),
            GameEvent(8, id1, MOVE_PLAYED, Player.O, BottomEnd, now.plusSeconds(6)),
        )

        val actual = dao.getGameHistory(id1)

        assertThat(actual).isInOrder(comparator)
    }

    @Test
    fun failToInsertWithDuplicatedId() = testScope.runTest {
        val duplicated = listOf(
            GameEvent(99L, "id1", STARTED, Player.X, TopEnd, Instant.now()),
            GameEvent(99L, "id2", STARTED, Player.O, TopEnd, Instant.now()),
        )

        dao.insert(duplicated[0])

        assertFailsWith<SQLiteConstraintException> { dao.insert(duplicated[1]) }
    }
}