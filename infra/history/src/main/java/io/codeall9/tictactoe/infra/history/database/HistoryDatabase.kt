package io.codeall9.tictactoe.infra.history.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.codeall9.tictactoe.infra.history.transform.InstantConverter

@Database(
    entities = [GameEvent::class],
    version = 2,
    autoMigrations = [
    ],
    exportSchema = true,
)
@TypeConverters(InstantConverter::class)
internal abstract class HistoryDatabase: RoomDatabase() {

    abstract fun gameEventDao(): GameEventDao

    companion object
}
