package io.codeall9.history.transform

import io.codeall9.history.GameId
import io.codeall9.history.GameTied
import io.codeall9.history.PlayedGame
import io.codeall9.history.PlayerWon
import io.codeall9.history.persistence.GameWinner

internal typealias GameWinnerToPlayedGame = GameWinner.() -> PlayedGame

internal fun GameWinner.toPlayedGame(): PlayedGame {
    val id = GameId(gameId)
    return winner
        ?.let { player -> PlayerWon(id, player) }
        ?: run { GameTied(id) }
}
