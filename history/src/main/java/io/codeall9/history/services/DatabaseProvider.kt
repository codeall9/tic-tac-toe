package io.codeall9.history.services

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import io.codeall9.history.persistence.HistoryDatabase
import io.codeall9.history.room.InstantConverter


internal typealias DatabaseProvider = () -> HistoryDatabase
private const val DB_FILE_NAME = "history.db"

@Volatile
private var database: HistoryDatabase? = null

internal inline val HistoryDatabase.Companion.INSTANCE get() = database

internal fun singletonHistoryDb(
    appContext: Context,
    init: (Context) -> HistoryDatabase = ::initInMemoryDb,
): DatabaseProvider = {
    HistoryDatabase.INSTANCE ?: synchronized(HistoryDatabase::class) {
        database ?: init(appContext).also { database = it }
    }
}

internal inline fun buildInMemoryDb(
    appContext: Context,
    builder: RoomDatabase.Builder<HistoryDatabase>.() -> Unit = { },
): HistoryDatabase {
    return Room
        .inMemoryDatabaseBuilder(appContext, HistoryDatabase::class.java)
        .apply(builder)
        .build()
}

internal inline fun buildLocalDb(
    appContext: Context,
    builder: RoomDatabase.Builder<HistoryDatabase>.() -> Unit = { },
): HistoryDatabase {
    return Room
        .databaseBuilder(appContext, HistoryDatabase::class.java, DB_FILE_NAME)
        .apply(builder)
        .build()
}

internal fun initLocalDb(appContext: Context): HistoryDatabase {
    return buildLocalDb(appContext) { addTypeConverter(InstantConverter()) }
}

private fun initInMemoryDb(appContext: Context): HistoryDatabase {
    return buildInMemoryDb(appContext) { addTypeConverter(InstantConverter()) }
}