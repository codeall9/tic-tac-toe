package io.codeall9.history.persistence

import androidx.room.Dao
import androidx.room.Query
import io.codeall9.history.room.InsertableDao

@Dao
internal interface GameEventDao: InsertableDao<GameEvent> {

    @Query("SELECT * FROM GameEvent WHERE event_id = :eventId")
    suspend fun getGameEvent(eventId: Long): GameEvent

    @Query("""
        SELECT * FROM GameEvent 
        WHERE game_id = :gameId
        ORDER BY created_at ASC, event_id ASC
    """)
    suspend fun getGameHistory(gameId: String): List<GameEvent>
}
