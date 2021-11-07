package io.codeall9.history.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.codeall9.engine.model.CellPosition
import io.codeall9.engine.model.Player
import java.time.Instant

@Entity(tableName = "GameEvent")
internal data class GameEvent(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "event_id")
    val eventId: Long,
    @ColumnInfo(name = "game_id", index = true)
    val gameId: String,
    @ColumnInfo(name = "event_type")
    val eventType: EventType,
    @ColumnInfo(name = "player")
    val player: Player?,
    @ColumnInfo(name = "position")
    val position: CellPosition?,
    @ColumnInfo(name = "created_at", index = true)
    val createdAt: Instant,
) {
    constructor(
        gameId: String,
        eventType: EventType,
        player: Player?,
        position: CellPosition?,
        eventId: Long = 0,
    ): this(eventId, gameId, eventType, player, position, Instant.now())

    enum class EventType(val type: Int) {
        STARTED(0),
        MOVE_PLAYED(1);
    }
}