package io.codeall9.history.repository

import io.codeall9.engine.model.Player
import io.codeall9.history.GameId
import io.codeall9.history.GameTied
import io.codeall9.history.PlayerWon
import io.codeall9.history.persistance.MockedGameWinnerDao
import io.codeall9.history.persistence.GameWinner
import io.codeall9.history.transform.GameWinnerToPlayedGame
import io.codeall9.history.transform.toPlayedGame
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class PlayedGameRepositoryTest {

    @Test
    @DisplayName("fallback to empty list if an exception is thrown from `Dao`")
    fun handleDaoFailure() = runTest {
        val dao = object : MockedGameWinnerDao() {
            override fun getRecentWinners(total: Int): Flow<List<GameWinner>> {
                error("mock failure")
            }
        }

        val getPlayedList = dao.queryPlayedGames()
        dao.insertPlayedGame()
            .invoke(PlayerWon(GameId("890"), Player.X))

        val actual = getPlayedList(10U).single()

        assertEquals(emptyList(), actual)
    }

    @Test
    @DisplayName("Ignore element that is fail to transform")
    fun handleTransformFailure() = runTest {
        val dao = object : MockedGameWinnerDao() {}

        val failedIds = listOf("103", "105", "107")
        val toDomain: GameWinnerToPlayedGame = {
            if (gameId in failedIds) error("mock failure") else toPlayedGame()
        }

        val getPlayedList = dao.queryPlayedGames(toDomain)
        val append = dao.insertPlayedGame()

        val data = listOf(
            PlayerWon(GameId("101"), Player.O),
            GameTied(GameId("102")),
            GameTied(GameId("103")),
            PlayerWon(GameId("104"), Player.X),
            PlayerWon(GameId("105"), Player.X),
            GameTied(GameId("106")),
            PlayerWon(GameId("107"), Player.O),
        )

        val expected = data
            .onEach { append(it) }
            .filterNot { it.gameId.value in failedIds }

        val actual = getPlayedList(data.size.toUInt()).single()

        assertEquals(expected, actual)
    }
}