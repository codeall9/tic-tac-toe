package io.codeall9.tictactoe.infra.history.factory

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import io.codeall9.tictactoe.infra.history.database.HistoryDatabase
import io.codeall9.tictactoe.infra.history.transform.InstantConverter
import java.util.concurrent.TimeUnit


private const val DB_FILE_NAME = "history.db"

@Volatile
private var database: HistoryDatabase? = null

internal inline val HistoryDatabase.Companion.INSTANCE get() = database

internal fun singletonHistoryDb(
    appContext: Context,
    init: (Context) -> HistoryDatabase = ::initInMemoryDb,
): HistoryDatabase {
    return HistoryDatabase.INSTANCE ?: synchronized(HistoryDatabase::class) {
        database ?: init(appContext).also { database = it }
    }
}

internal fun initLocalDb(appContext: Context): HistoryDatabase {
    return buildLocalDb(appContext) {
        addTypeConverter(InstantConverter())
        fallbackToDestructiveMigration()
        setAutoCloseTimeout(2, TimeUnit.MINUTES)
    }
}

internal fun initInMemoryDb(appContext: Context): HistoryDatabase {
    return buildInMemoryDb(appContext) { addTypeConverter(InstantConverter()) }
}

private inline fun buildLocalDb(
    appContext: Context,
    builder: RoomDatabase.Builder<HistoryDatabase>.() -> Unit = { },
): HistoryDatabase {
    return Room
        .databaseBuilder(appContext, HistoryDatabase::class.java, DB_FILE_NAME)
        .apply(builder)
        .build()
}

private inline fun buildInMemoryDb(
    appContext: Context,
    builder: RoomDatabase.Builder<HistoryDatabase>.() -> Unit = { },
): HistoryDatabase {
    return Room
        .inMemoryDatabaseBuilder(appContext, HistoryDatabase::class.java)
        .apply(builder)
        .build()
}