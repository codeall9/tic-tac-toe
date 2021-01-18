package io.codeall9.tictactoe.model

import androidx.compose.runtime.Immutable

@Immutable
data class Player(
    val name: String,
    val flag: Char,
) {
    companion object {
        val O = Player("Player O", 'O')
        val X = Player("Player X", 'X')
    }
}