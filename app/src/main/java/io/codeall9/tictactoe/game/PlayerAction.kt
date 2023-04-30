package io.codeall9.tictactoe.game

import io.codeall9.tictactoe.core.engine.model.CellPosition

sealed class PlayerAction

data class MarkPosition(val position: CellPosition): PlayerAction()
object LaunchNewGame: PlayerAction()