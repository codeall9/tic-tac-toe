package io.codeall9.history.services

import android.content.Context
import io.codeall9.history.GameLog
import io.codeall9.history.LogGameTransition
import io.codeall9.history.repository.appendLog
import io.codeall9.history.transform.toGameEvent


public fun provideGameLogger(appContext: Context): LogGameTransition {
    return provideGameLogger(singletonHistoryDb(appContext, ::initLocalDb))
}

internal inline fun provideGameLogger(getHistoryDb: DatabaseProvider): LogGameTransition {
    return getHistoryDb()
        .gameEventDao()
        .appendLog(GameLog::toGameEvent)
}