package io.codeall9.history.persistence

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import io.codeall9.engine.model.Player
import io.codeall9.history.room.InstantConverter
import io.codeall9.history.services.buildInMemoryDb
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import java.io.IOException
import java.time.Instant
import kotlin.test.*

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
internal class GameWinnerDaoTest {

    private lateinit var db: HistoryDatabase
    private lateinit var dao: GameWinnerDao
    private val testScope = TestScope()
    private val testDispatcher = StandardTestDispatcher(testScope.testScheduler, name = "IO dispatcher")

    @BeforeTest
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = buildInMemoryDb(context) {
            setQueryExecutor(testDispatcher.asExecutor())
            addTypeConverter(InstantConverter())
        }
        dao = db.gameWinnerDao()
    }

    @AfterTest
    @Throws(IOException::class)
    fun tearDown() {
        db.close()
    }

    @Test
    fun entityIsEqualToOriginal() = testScope.runTest {
        val original = GameWinner("51", Player.X)

        dao.insert(original)
        val actual = dao.getGameWinner(original.gameId)

        assertEquals(original, actual)
    }

    @Test
    fun maximumListSizeIs5() = testScope.runTest {
        val total = 5

        assertThat(dao.getRecentWinners(total).first()).isEmpty()

        dao.insert(GameWinner("101", null))

        assertThat(dao.getRecentWinners(total).first()).hasSize(1)

        dao.insert(
            GameWinner("102", Player.O),
            GameWinner("103", Player.X),
            GameWinner("104", null),
            GameWinner("105", Player.O),
            GameWinner("106", null),
        )
        assertThat(dao.getRecentWinners(total).first()).hasSize(total)
    }

    @Test
    fun resultIsContainsTheOriginalItems() = testScope.runTest {
        val original = arrayOf(
            GameWinner("101", null),
            GameWinner("102", Player.O),
            GameWinner("103", Player.X),
            GameWinner("104", null),
            GameWinner("105", Player.O),
        )

        dao.insert(*original)
        val actual = dao.getRecentWinners(original.size).first()

        assertThat(actual).containsExactlyElementsIn(original)
    }

    @Test
    fun listIsSortedByTimeWithDecs() = testScope.runTest {
        val now = Instant.now()
        val comparator = Comparator<GameWinner> { first, second ->
            second.createdAt.compareTo(first.createdAt)
        }

        dao.insert(
            GameWinner("104", null, now.plusSeconds(4)),
            GameWinner("105", Player.O, now.plusSeconds(5)),
            GameWinner("103", Player.X, now.plusSeconds(3)),
            GameWinner("101", null, now.plusSeconds(1)),
            GameWinner("102", Player.O, now.plusSeconds(2)),
        )
        val actual = dao.getRecentWinners(5).first()

        assertThat(actual).isInOrder(comparator)
    }

    @Test
    fun failToInsertDuplicatedId() = testScope.runTest {
        dao.insert(GameWinner("50", Player.O))

        assertFailsWith<SQLiteConstraintException> {
            dao.insert(GameWinner("50", null))
        }
    }
}