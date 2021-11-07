package io.codeall9.history

import android.content.Context
import io.codeall9.engine.TicTacToeInitializer
import io.codeall9.engine.model.*
import io.codeall9.history.services.initLocalDb
import io.codeall9.history.services.provideAddPlayedGame
import io.codeall9.history.services.provideGameLogger
import io.codeall9.history.services.singletonHistoryDb
import java.util.*


/**
 * Receive Tic Tac Toe events
 *
 * @param launchGame TicTacToeInitializer
 * @param onStateChanged invoke when game state changed
 * @param onResult invoke when game over
 *
 * @return [TicTacToeInitializer]
 */
public fun recordingTicTacToe(
    launchGame: TicTacToeInitializer,
    onStateChanged: suspend (GameLog) -> Unit,
    onResult: suspend (PlayedGame) -> Unit,
): TicTacToeInitializer {
    return { player ->
        val gameId = UUID.randomUUID().toGameId()
        val record = recordingGameState(gameId, onStateChanged, onResult)

        launchGame(player)
            .also { onStateChanged(GameStarted(gameId, player)) }
            .let { record(it) }
    }
}

/**
 * Store Tic Tac Toe commands into database
 *
 * @param appContext applicationContext
 * @param launchGame TicTacToeInitializer
 *
 * @return [TicTacToeInitializer]
 */
public fun recordingTicTacToe(
    appContext: Context,
    launchGame: TicTacToeInitializer,
): TicTacToeInitializer {
    val db = singletonHistoryDb(appContext, ::initLocalDb)
    val gameLogger: LogGameTransition = provideGameLogger(db)
    val addPlayedGame: AddPlayedGame = provideAddPlayedGame(db)
    return recordingTicTacToe(launchGame, gameLogger, addPlayedGame)
}

private fun recordingGameState(
    gameId: GameId,
    onPlayerMove: suspend (MovePlayed) -> Unit,
    onMatchOver: suspend (PlayedGame) -> Unit,
): suspend GameState.() -> GameState {
    val recordMove: (Player, CellPosition, Markable) -> Markable = { player, position, mark ->
        {
            val record = recordingGameState(gameId, onPlayerMove, onMatchOver)
            mark()
                .also { onPlayerMove(MovePlayed(gameId, player, position)) }
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
            is GameTie -> this.also { onMatchOver(GameTied(gameId)) }
            is GameWon -> this.also { onMatchOver(PlayerWon(gameId, winner)) }
        }
    }
}
