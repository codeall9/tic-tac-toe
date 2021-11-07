package io.codeall9.history.services

import android.content.Context
import io.codeall9.history.GetRecentPlayedGames
import io.codeall9.history.persistence.GameWinner
import io.codeall9.history.repository.queryPlayedGames
import io.codeall9.history.transform.toPlayedGame


public fun provideRecentPlayedGame(
    appContext: Context,
    onError: LogNonFatalException = provideDebugLogger("GetRecentPlayedGames"),
): GetRecentPlayedGames {
    return provideRecentPlayedGame(
        singletonHistoryDb(appContext, ::initLocalDb),
        onError,
    )
}

internal inline fun provideRecentPlayedGame(
    getHistoryDb: DatabaseProvider,
    crossinline onError: LogNonFatalException,
): GetRecentPlayedGames {
    val dao = getHistoryDb().gameWinnerDao()
    return dao.queryPlayedGames(
        GameWinner::toPlayedGame,
        onError,
    )
}