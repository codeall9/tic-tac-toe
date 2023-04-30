package io.codeall9.tictactoe.infra.history.database

import androidx.room.Dao
import androidx.room.Query
import io.codeall9.tictactoe.infra.history.InsertableDao
import kotlinx.coroutines.flow.Flow

@Dao
internal interface GameEventDao: InsertableDao<GameEvent> {

    @Query("SELECT * FROM GameEvent WHERE event_id = :eventId")
    suspend fun getGameEvent(eventId: Long): GameEvent

    @Query("""
        SELECT * FROM GameEvent 
        WHERE history_id = :historyId
        ORDER BY created_at ASC, event_id ASC
    """)
    suspend fun getGameHistory(historyId: String): List<GameEvent>

    @Query("""
        SELECT * FROM GameEvent 
        WHERE event_type = "ENDED"
        ORDER BY created_at DESC, event_id DESC
        LIMIT :total
    """)
    fun getRecentResults(total: Int): Flow<List<GameEvent>>

    @Query("""
        SELECT * FROM GameEvent 
        WHERE event_type = "ENDED" AND history_id = :historyId
    """)
    fun getGameResult(historyId: String): Flow<GameEvent>
}