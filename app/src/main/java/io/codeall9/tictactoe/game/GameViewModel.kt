package io.codeall9.tictactoe.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import io.codeall9.tictactoe.core.engine.LaunchTicTacToe
import io.codeall9.tictactoe.core.engine.initLocalGame
import io.codeall9.tictactoe.core.engine.model.CellPosition
import io.codeall9.tictactoe.core.engine.model.GameState
import io.codeall9.tictactoe.core.engine.model.GameTie
import io.codeall9.tictactoe.core.engine.model.GameWon
import io.codeall9.tictactoe.core.engine.model.Player
import io.codeall9.tictactoe.core.engine.model.PlayerOTurn
import io.codeall9.tictactoe.core.engine.model.PlayerXTurn
import io.codeall9.tictactoe.core.engine.model.markOrNull
import io.codeall9.tictactoe.model.GameBox
import io.codeall9.tictactoe.model.toBoxList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.onFailure

class GameViewModel(
    private val initGame: LaunchTicTacToe = initLocalGame,
    private val worker: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel() {

    private val gameState = MutableLiveData<GameState>()

    private val gameActor = viewModelScope.actor<PlayerAction>(
        context = worker,
        capacity = Channel.RENDEZVOUS,
    ) {
        var current = initGame(Player.O).also { gameState.postValue(it) }
        for (action in channel) {
            val newState = when (action) {
                is MarkPosition -> current.onPlayerMove(action.position)
                is LaunchNewGame -> initGame(Player.O)
            }
            if (newState != null) {
                current = newState.also { gameState.postValue(it) }
            }
            Log.d("TicTacToe", "gameActor: $action")
        }
    }

    val gameBoxes: LiveData<List<GameBox>> = gameState.map { result ->
        result.board.toBoxList()
    }

    val gameStatus: LiveData<GameStatus> = gameState
        .map {
            when (it) {
                is GameTie -> GameStatus.IsDraw
                is GameWon -> GameStatus.PlayerWin(it.winner)
                is PlayerOTurn -> GameStatus.Ongoing
                is PlayerXTurn -> GameStatus.Ongoing
            }
        }

    fun onActionDispatched(action: PlayerAction) {
        gameActor.trySend(action)
            .onFailure { Log.e("TicTacToe", "unable to send $action", it) }
    }

    private suspend fun GameState.onPlayerMove(position: CellPosition): GameState? {
        return when (this) {
            is PlayerOTurn -> markOrNull(position)
            is PlayerXTurn -> markOrNull(position)
            else -> null
        }
    }
}