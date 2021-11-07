package io.codeall9.history.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.codeall9.engine.model.Player
import java.time.Instant

@Entity(tableName = "GameWinner")
internal data class GameWinner(
    @PrimaryKey
    @ColumnInfo(name = "game_id")
    val gameId: String,
    @ColumnInfo(name = "player")
    val winner: Player?,
    @ColumnInfo(name = "created_at", index = true)
    val createdAt: Instant,
) {
    constructor(
        gameId: String,
        winner: Player?,
    ): this(gameId, winner, Instant.now())
}
