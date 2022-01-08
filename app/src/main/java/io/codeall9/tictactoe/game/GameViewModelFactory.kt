package io.codeall9.tictactoe.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.codeall9.engine.TicTacToeInitializer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class GameViewModelFactory(
    private val initGame: TicTacToeInitializer,
    private val worker: CoroutineDispatcher = Dispatchers.Default,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(initGame, worker) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
