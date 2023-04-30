package io.codeall9.core.history.util

import io.codeall9.tictactoe.core.engine.model.GameState
import io.codeall9.tictactoe.core.engine.model.Player
import io.codeall9.tictactoe.core.engine.model.PlayerOTurn
import io.codeall9.tictactoe.core.engine.model.PlayerXTurn

internal fun initFakeGame(player: Player): GameState {
    return when (player) {
        Player.O -> PlayerOTurn(randomBoard(), randomValidRounds(1))
        Player.X -> PlayerXTurn(randomBoard(), randomValidRounds(1))
    }
}