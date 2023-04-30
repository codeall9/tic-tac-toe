package io.codeall9.tictactoe.infra.history.factory

import android.content.Context
import io.codeall9.core.history.repository.GetGameHistory
import io.codeall9.core.history.repository.GetRecentGameResult
import io.codeall9.core.history.repository.LogGameTransition
import io.codeall9.tictactoe.infra.history.comman.HandleException
import io.codeall9.tictactoe.infra.history.repository.GetGameHistory as GetGameHistoryImpl1
import io.codeall9.tictactoe.infra.history.repository.GetRecentGameResult as GetRecentGameResultImpl1
import io.codeall9.tictactoe.infra.history.repository.LogGameTransition as LogGameTransitionImpl1

public fun provideLogGameTransition(
    appContext: Context,
    handleError: HandleException = logAndroidError,
): LogGameTransition {
    val dao = singletonHistoryDb(appContext, ::initLocalDb)
        .gameEventDao()
    return LogGameTransitionImpl1(dao, handleError)
}

public fun provideGetGameHistory(
    appContext: Context,
    handleError: HandleException = logAndroidError,
): GetGameHistory {
    val dao = singletonHistoryDb(appContext, ::initLocalDb)
        .gameEventDao()
    return GetGameHistoryImpl1(dao, handleError)
}

public fun provideGetRecentGameResult(
    appContext: Context,
    handleError: HandleException = logAndroidError,
): GetRecentGameResult {
    val dao = singletonHistoryDb(appContext, ::initLocalDb)
        .gameEventDao()
    return GetRecentGameResultImpl1(dao, handleError)
}
