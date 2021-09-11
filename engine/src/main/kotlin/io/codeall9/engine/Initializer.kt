package io.codeall9.engine

import io.codeall9.engine.model.GameState
import io.codeall9.engine.model.Player

typealias TicTacToeInitializer = suspend (Player) -> GameState
