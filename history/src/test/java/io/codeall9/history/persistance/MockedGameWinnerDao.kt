package io.codeall9.history.persistance

import io.codeall9.history.persistence.GameWinner
import io.codeall9.history.persistence.GameWinnerDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal abstract class MockedGameWinnerDao: GameWinnerDao {
    private var idCount = 0L
    private val table = mutableMapOf<String, GameWinner>()

    override suspend fun insert(entity: GameWinner): Long {
        table[entity.gameId] = entity
        return idCount++
    }

    override suspend fun insert(vararg entities: GameWinner): List<Long> {
        entities.associateByTo(table) { it.gameId }
        return List(entities.size) { idCount++ }
    }

    override suspend fun getGameWinner(gameId: String): GameWinner {
        return requireNotNull(table[gameId])
    }

    override fun getRecentWinners(total: Int): Flow<List<GameWinner>> {
        return table.asSequence()
            .map { it.value }
            .sortedByDescending { it.createdAt }
            .take(total)
            .toList()
            .run { flowOf(this) }
    }
}