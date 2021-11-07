package io.codeall9.history.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.codeall9.history.room.InstantConverter

@Database(entities = [GameEvent::class, GameWinner::class], version = 1)
@TypeConverters(InstantConverter::class)
internal abstract class HistoryDatabase: RoomDatabase() {

    abstract fun gameEventDao(): GameEventDao

    abstract fun gameWinnerDao(): GameWinnerDao

    companion object
}
