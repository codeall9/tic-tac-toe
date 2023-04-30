package io.codeall9.tictactoe.infra.history.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.codeall9.tictactoe.core.engine.model.CellPosition
import io.codeall9.tictactoe.core.engine.model.Player
import java.time.Instant

@Entity(tableName = "GameEvent")
internal data class GameEvent(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "event_id")
    val eventId: Long,
    @ColumnInfo(name = "history_id", index = true)
    val historyId: String,
    @ColumnInfo(name = "event_type")
    val eventType: EventType,
    @ColumnInfo(name = "player")
    val player: Player?,
    @ColumnInfo(name = "position")
    val position: CellPosition?,
    @ColumnInfo(name = "created_at")
    val createdAt: Instant,
) {
    constructor(
        eventType: EventType,
        historyId: String,
        player: Player?,
        position: CellPosition?,
        eventId: Long = 0,
        createdAt: Instant = Instant.now(),
    ): this(eventId, historyId, eventType, player, position, createdAt)

    enum class EventType {
        STARTED,
        MOVE_PLAYED,
        ENDED,
        ;
    }
}