package io.codeall9.tictactoe.infra.history

import androidx.room.Delete

internal interface DeletableDao<T> {

    @Delete
    suspend fun delete(entity: T)
}