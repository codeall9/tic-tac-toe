package io.codeall9.tictactoe.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.codeall9.tictactoe.model.Player

class GameViewModel: ViewModel() {

    private var match = TicTacToeMatch(Player.O, Player.X)

    var boardState by mutableStateOf(emptyBoard())

    var isGameOverState by mutableStateOf(false)

    var winnerState by mutableStateOf<Player?>(null)

    fun onPlayerMove(index: Int) {
        if (isGameOverState || boardState[index] != ' ') return

        boardState[index] = match.currentPlayer.flag

        updateGameStatus()
    }

    fun resetGame() {
        boardState = emptyBoard()
        match = TicTacToeMatch(Player.O, Player.X)
        isGameOverState = false
    }

    private fun emptyBoard() = mutableStateListOf(
        ' ', ' ', ' ',
        ' ', ' ', ' ',
        ' ', ' ', ' ',
    )

    private fun updateGameStatus() {
        when {
            match.checkIsGameOver(boardState) -> {
                isGameOverState = true
                winnerState = match.currentPlayer
            }
            match.isEnded -> {
                isGameOverState = true
                winnerState = null
            }
            else -> {
                match.nextRound()
            }
        }
    }
}