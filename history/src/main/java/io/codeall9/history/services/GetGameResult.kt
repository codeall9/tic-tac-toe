package io.codeall9.history.services

import android.content.Context
import io.codeall9.history.GetGameResult
import io.codeall9.history.persistence.GameWinner
import io.codeall9.history.repository.queryPlayedGame
import io.codeall9.history.transform.toPlayedGame

public fun provideGetGameResult(appContext: Context): GetGameResult {
    return provideGetGameResult(singletonHistoryDb(appContext, ::initLocalDb))
}

internal inline fun provideGetGameResult(
    getHistoryDb: DatabaseProvider
): GetGameResult {
    return getHistoryDb()
        .gameWinnerDao()
        .queryPlayedGame(GameWinner::toPlayedGame)
}