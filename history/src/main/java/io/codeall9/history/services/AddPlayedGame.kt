package io.codeall9.history.services

import io.codeall9.history.AddPlayedGame
import io.codeall9.history.PlayedGame
import io.codeall9.history.repository.insertPlayedGame
import io.codeall9.history.transform.toGameWinner


internal fun provideAddPlayedGame(
    getHistoryDb: DatabaseProvider,
): AddPlayedGame {
    val dao = getHistoryDb().gameWinnerDao()
    return dao.insertPlayedGame(PlayedGame::toGameWinner)
}