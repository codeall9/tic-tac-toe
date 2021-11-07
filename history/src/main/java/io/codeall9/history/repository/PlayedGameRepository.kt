package io.codeall9.history.repository

import io.codeall9.history.AddPlayedGame
import io.codeall9.history.GetGameResult
import io.codeall9.history.GetRecentPlayedGames
import io.codeall9.history.PlayedGame
import io.codeall9.history.persistence.GameWinner
import io.codeall9.history.persistence.GameWinnerDao
import io.codeall9.history.services.LogNonFatalException
import io.codeall9.history.transform.GameWinnerToPlayedGame
import io.codeall9.history.transform.PlayedGameToGameWinner
import io.codeall9.history.transform.toGameWinner
import io.codeall9.history.transform.toPlayedGame
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map


internal inline fun GameWinnerDao.insertPlayedGame(
    crossinline toEntity: PlayedGameToGameWinner = PlayedGame::toGameWinner
): AddPlayedGame = { result ->
    insert(result.toEntity())
}

internal inline fun GameWinnerDao.queryPlayedGame(
    crossinline toDomain: GameWinnerToPlayedGame = GameWinner::toPlayedGame,
): GetGameResult = {
    getGameWinner(it.value).toDomain()
}

internal inline fun GameWinnerDao.queryPlayedGames(
    crossinline toDomain: GameWinnerToPlayedGame = GameWinner::toPlayedGame,
    crossinline onError: LogNonFatalException = {},
): GetRecentPlayedGames = {
    runCatching { getRecentWinners(it.toInt()) }
        .onFailure(onError)
        .getOrElse { flowOf(emptyList()) }
        .map {
            it.asSequence()
                .map { winner -> winner.runCatching(toDomain).onFailure(onError) }
                .mapNotNull { result -> result.getOrNull() }
                .toList()
        }
}