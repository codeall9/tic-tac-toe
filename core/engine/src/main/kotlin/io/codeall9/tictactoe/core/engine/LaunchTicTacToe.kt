package io.codeall9.tictactoe.core.engine

import io.codeall9.tictactoe.core.engine.model.GameState
import io.codeall9.tictactoe.core.engine.model.Player

public fun interface LaunchTicTacToe: suspend (Player) -> GameState

public val initLocalGame: LaunchTicTacToe = LaunchTicTacToe { initGame(it) }
