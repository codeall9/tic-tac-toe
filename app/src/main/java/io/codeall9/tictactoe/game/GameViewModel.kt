package io.codeall9.tictactoe.game

import android.util.Log
import androidx.lifecycle.*
import io.codeall9.tictactoe.TicTacToeInitializer
import io.codeall9.tictactoe.initLocalGame
import io.codeall9.tictactoe.model.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

private val boxOrders = listOf(
    CellPosition.TopStart,
    CellPosition.TopCenter,
    CellPosition.TopEnd,
    CellPosition.CenterStart,
    CellPosition.Center,
    CellPosition.CenterEnd,
    CellPosition.BottomStart,
    CellPosition.BottomCenter,
    CellPosition.BottomEnd,
)

data class Box(val position: CellPosition, val cell: Cell)

class GameViewModel(
    private val initGame: TicTacToeInitializer = initLocalGame,
    private val workerDispatcher: CoroutineDispatcher = Dispatchers.Default,
): ViewModel() {

    private val gameState = MutableLiveData<MatchResult>()

    val gridBoxes: LiveData<List<Box>> = gameState.map { result ->
        boxOrders.map { position ->  Box(position, result.board[position]) }
    }

    val gameWinner: LiveData<Player?> = gameState
        .map { result ->
            result.let { it as? GameOver }
                ?.winner
        }
        .distinctUntilChanged()

    val gameTie: LiveData<Boolean> = gameState.map { it is GameTie }

    init {
        viewModelScope.launchNewGame()
    }

    fun onPlayerMove(position: CellPosition) {
        val current = gameState.value ?: return
        viewModelScope.launch(workerDispatcher) {
            current.runCatching { onPlayerMove(position) }
                .onSuccess { newState -> gameState.postValue(newState) }
                .onFailure { Log.e("TicTacToe", "action is rejected", it) }
        }
    }

    fun restartGame() {
        viewModelScope.launchNewGame()
    }

    @Throws(IllegalStateException::class, IllegalArgumentException::class)
    private suspend fun MatchResult.onPlayerMove(position: CellPosition): MatchResult {
        return when (this) {
            is PlayerOTurn -> {
                requireNotNull(actions[position]) { "$position is not markable" }
                    .run { invoke() }
            }
            is PlayerXTurn -> {
                requireNotNull(actions[position]) { "$position is not markable" }
                    .run { invoke() }
            }
            else -> this
        }
    }

    private fun CoroutineScope.launchNewGame(
        context: CoroutineContext = workerDispatcher,
        first: Player = Player.O,
    ) = launch(context) {
        gameState.postValue(initGame(first))
    }
}