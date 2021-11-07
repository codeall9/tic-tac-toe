package io.codeall9.history.persistence

import androidx.room.Dao
import androidx.room.Query
import io.codeall9.history.room.InsertableDao
import kotlinx.coroutines.flow.Flow

@Dao
internal interface GameWinnerDao: InsertableDao<GameWinner> {

    @Query("SELECT * FROM GameWinner WHERE game_id = :gameId")
    suspend fun getGameWinner(gameId: String): GameWinner

    @Query("""
        SELECT * FROM GameWinner
        ORDER BY created_at DESC
        LIMIT :total
    """)
    fun getRecentWinners(total: Int): Flow<List<GameWinner>>
}