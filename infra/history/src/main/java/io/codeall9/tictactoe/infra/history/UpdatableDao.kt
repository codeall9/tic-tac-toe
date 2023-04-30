package io.codeall9.tictactoe.infra.history

import androidx.room.Update

internal interface UpdatableDao<T> {

    @Update
    fun update(entity: T)
}