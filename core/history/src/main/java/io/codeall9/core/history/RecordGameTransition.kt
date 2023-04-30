package io.codeall9.core.history

import io.codeall9.core.history.event.GameStarted
import io.codeall9.core.history.event.GameTied
import io.codeall9.core.history.event.GameTransition
import io.codeall9.core.history.event.MovePlayed
import io.codeall9.core.history.event.PlayerWon
import io.codeall9.core.history.model.HistoryId
import io.codeall9.tictactoe.core.engine.LaunchTicTacToe
import io.codeall9.tictactoe.core.engine.model.CellPosition
import io.codeall9.tictactoe.core.engine.model.GameState
import io.codeall9.tictactoe.core.engine.model.GameTie
import io.codeall9.tictactoe.core.engine.model.GameWon
import io.codeall9.tictactoe.core.engine.model.Markable
import io.codeall9.tictactoe.core.engine.model.Player
import io.codeall9.tictactoe.core.engine.model.PlayerOTurn
import io.codeall9.tictactoe.core.engine.model.PlayerXTurn
import java.util.UUID

/**
 * Receive Tic Tac Toe events
 *
 * @param launchGame TicTacToeInitializer
 * @param onStateChanged invoke when game state changed
 *
 * @return [LaunchTicTacToe]
 */
internal fun recordGameTransition(
    launchGame: LaunchTicTacToe,
    onStateChanged: suspend (GameTransition) -> Unit,
): LaunchTicTacToe {
    return LaunchTicTacToe { player ->
        val id = HistoryId.of(UUID.randomUUID())
        val record = recordGameState(id, onStateChanged)

        launchGame(player)
            .also { onStateChanged(GameStarted(id, player)) }
            .let { record(it) }
    }
}

private fun recordGameState(
    historyId: HistoryId,
    onStateChanged: suspend (GameTransition) -> Unit,
): suspend GameState.() -> GameState {
    val recordMove: (Player, CellPosition, Markable) -> Markable = { player, position, mark ->
        {
            val record = recordGameState(historyId, onStateChanged)
            mark()
                .also { onStateChanged(MovePlayed(historyId, player, position)) }
                .let { record(it) }
        }
    }
    return {
        when (this) {
            is PlayerOTurn -> actions
                .mapValues { (position, move) -> recordMove(Player.O, position, move) }
                .let { PlayerOTurn(board, it) }
            is PlayerXTurn -> actions
                .mapValues { (position, move) -> recordMove(Player.X, position, move) }
                .let { PlayerXTurn(board, it) }
            is GameTie -> this.also { onStateChanged(GameTied(historyId)) }
            is GameWon -> this.also { onStateChanged(PlayerWon(historyId, winner)) }
        }
    }
}

