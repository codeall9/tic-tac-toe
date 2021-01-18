package io.codeall9.tictactoe.game

import io.codeall9.tictactoe.model.Player

class TicTacToeMatch(
    val player1: Player,
    val player2: Player,
    var currentPlayer: Player = player1,
    var initRound: Int = 0
) {
    var round = initRound
        private set

    val isEnded get() = round >= 8

    fun nextRound() {
        round++
        switchPlayer()
    }

    fun checkIsGameOver(board: List<Char>): Boolean {
        return checkBoardRows(board) || checkBoardColumns(board) || checkBoardDiagonals(board)
    }

    private fun switchPlayer() {
        currentPlayer = if (currentPlayer == player1) player2 else player1
    }

    private fun checkBoardRows(board: List<Char>): Boolean {
        for (i in 0..2) {
            val first = board[i * 3]
            val second = board[i * 3 + 1]
            val third = board[i * 3 + 2]
            if (first == second && first == third && first != ' ') {
                return true
            }
        }
        return false
    }

    private fun checkBoardColumns(board: List<Char>): Boolean {
        for (j in 0..2) {
            val first = board[0 + j]
            val second = board[3 + j]
            val third = board[6 + j]
            if (first == second && first == third && first != ' ') {
                return true
            }
        }
        return false
    }

    private fun checkBoardDiagonals(board: List<Char>): Boolean {
        if (board[0] == board[4] && board[0] == board[8] && board[0] != ' ') {
            return true
        }
        if (board[2] == board[4] && board[2] == board[6] && board[2] != ' ') {
            return true
        }
        return false
    }
}