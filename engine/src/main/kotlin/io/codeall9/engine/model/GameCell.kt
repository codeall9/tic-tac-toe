package io.codeall9.engine.model

sealed class Cell {
    data class Marked(val player: Player) : Cell()
    object Empty : Cell()

    companion object {
        val O = Marked(Player.O)
        val X = Marked(Player.X)
    }
}

enum class CellPosition {
    TopStart,
    TopCenter,
    TopEnd,
    CenterStart,
    Center,
    CenterEnd,
    BottomStart,
    BottomCenter,
    BottomEnd,
}