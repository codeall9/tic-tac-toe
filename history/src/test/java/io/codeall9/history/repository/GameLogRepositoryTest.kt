package io.codeall9.history.repository

import io.codeall9.engine.model.CellPosition.*
import io.codeall9.engine.model.Player
import io.codeall9.history.GameId
import io.codeall9.history.GameStarted
import io.codeall9.history.MovePlayed
import io.codeall9.history.persistance.MockedGameEventDao
import io.codeall9.history.persistence.GameEvent
import io.codeall9.history.persistence.GameEventDao
import io.codeall9.history.transform.GameEventToGameLog
import io.codeall9.history.transform.toGameLog
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GameLogRepositoryTest {

    @Test
    @DisplayName("fallback to empty list if an exception is thrown from `Dao`")
    fun handleDaoFailure() = runBlockingTest {
        val dao: GameEventDao = object : MockedGameEventDao() {
            override suspend fun getGameHistory(gameId: String): List<GameEvent> {
                error("mock failure")
            }
        }
        val getHistory = dao.queryGameLogs()

        dao.appendLog().invoke(GameStarted(GameId("1111"), Player.O))

        val actual = getHistory(GameId("1111"))

        assertEquals(emptyList(), actual)
    }

    @Test
    @DisplayName("Ignore element that is fail to transform")
    fun handleTransformFailure() = runBlockingTest {
        val dao: GameEventDao = object : MockedGameEventDao() {  }
        val toDomain: GameEventToGameLog = {
            if (eventId % 2 == 0L) error("mock failure") else toGameLog()
        }
        val append = dao.appendLog()
        val getHistory = dao.queryGameLogs(toDomain)

        val id = GameId("2222")
        val data = listOf(
            GameStarted(id, Player.O),
            MovePlayed(id, Player.O, Center),
            MovePlayed(id, Player.X, BottomEnd),
            MovePlayed(id, Player.O, TopStart),
            MovePlayed(id, Player.X, TopEnd),
            MovePlayed(id, Player.O, CenterEnd),
            MovePlayed(id, Player.X, BottomCenter),
            MovePlayed(id, Player.O, CenterStart),
        )
        val expected = data
            .onEach { append(it) }
            .filterIndexed { index, _ -> index % 2 != 0 }

        val actual = getHistory(id)

        assertEquals(expected, actual)
    }
}