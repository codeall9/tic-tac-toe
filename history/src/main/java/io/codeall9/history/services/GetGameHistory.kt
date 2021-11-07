package io.codeall9.history.services

import android.content.Context
import io.codeall9.history.GetGameHistory
import io.codeall9.history.persistence.GameEvent
import io.codeall9.history.repository.queryGameLogs
import io.codeall9.history.transform.toGameLog

public fun provideGetGameHistory(
    appContext: Context,
    onError: LogNonFatalException = provideDebugLogger("GetGameHistory"),
): GetGameHistory {
    return provideGetGameHistory(
        singletonHistoryDb(appContext, ::initLocalDb),
        onError
    )
}

internal inline fun provideGetGameHistory(
    getHistoryDb: DatabaseProvider,
    crossinline onError: LogNonFatalException,
): GetGameHistory {
    val dao = getHistoryDb().gameEventDao()
    return dao.queryGameLogs(
        toDomain = GameEvent::toGameLog,
        onError = onError,
    )
}