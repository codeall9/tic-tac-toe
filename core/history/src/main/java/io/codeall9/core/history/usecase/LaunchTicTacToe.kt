package io.codeall9.core.history.usecase

import io.codeall9.core.history.recordGameTransition
import io.codeall9.core.history.repository.LogGameTransition
import io.codeall9.tictactoe.core.engine.LaunchTicTacToe

public fun LaunchTicTacToe(
    logger: LogGameTransition,
    launchGame: LaunchTicTacToe,
): LaunchTicTacToe {
    return recordGameTransition(launchGame) { transition ->
        logger(transition)
    }
}