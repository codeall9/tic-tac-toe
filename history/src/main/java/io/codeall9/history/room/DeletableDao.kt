package io.codeall9.history.room

import androidx.room.Delete

internal interface DeletableDao<T> {

    @Delete
    suspend fun delete(entity: T)
}