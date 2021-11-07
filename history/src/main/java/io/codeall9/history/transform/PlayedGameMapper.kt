package io.codeall9.history.transform

import io.codeall9.history.GameTied
import io.codeall9.history.PlayedGame
import io.codeall9.history.PlayerWon
import io.codeall9.history.persistence.GameWinner

internal typealias PlayedGameToGameWinner = PlayedGame.() -> GameWinner

internal fun PlayedGame.toGameWinner(): GameWinner {
    return when(this) {
        is GameTied -> toEmptyWinner()
        is PlayerWon -> toWinner()
    }
}

private fun GameTied.toEmptyWinner(): GameWinner {
    return GameWinner(
        gameId = gameId.value,
        winner = null,
    )
}

private fun PlayerWon.toWinner(): GameWinner {
    return GameWinner(
        gameId = gameId.value,
        winner = winner
    )
}