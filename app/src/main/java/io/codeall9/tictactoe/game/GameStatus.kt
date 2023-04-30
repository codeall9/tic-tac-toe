package io.codeall9.tictactoe.game

import io.codeall9.tictactoe.core.engine.model.Player

sealed class GameStatus {
    object Ongoing : GameStatus()
    data class PlayerWin(val winner: Player) : GameStatus()
    object IsDraw : GameStatus()
}
