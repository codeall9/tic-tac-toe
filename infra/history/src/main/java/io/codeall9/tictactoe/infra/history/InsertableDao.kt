package io.codeall9.tictactoe.infra.history

import androidx.room.Insert

internal interface InsertableDao<T> {

    @Insert
    suspend fun insert(entity: T): Long

    @Insert
    suspend fun insert(vararg entities: T): List<Long>
}