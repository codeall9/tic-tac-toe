package io.codeall9.engine

import io.codeall9.engine.model.CellPosition
import io.codeall9.engine.model.GameState
import io.codeall9.engine.model.PlayerOTurn
import io.codeall9.engine.model.PlayerXTurn


val initLocalGame: TicTacToeInitializer = { initGame(it) }

suspend fun PlayerOTurn.markOrNull(position: CellPosition): GameState? {
    return runCatching { requireNotNull(actions[position]) }
        .mapCatching { mark -> mark() }
        .getOrNull()
}

suspend fun PlayerXTurn.markOrNull(position: CellPosition): GameState? {
    return runCatching { requireNotNull(actions[position]) }
        .mapCatching { mark -> mark() }
        .getOrNull()
}