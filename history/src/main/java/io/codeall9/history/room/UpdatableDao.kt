package io.codeall9.history.room

import androidx.room.Update

internal interface UpdatableDao<T> {

    @Update
    fun update(entity: T)
}