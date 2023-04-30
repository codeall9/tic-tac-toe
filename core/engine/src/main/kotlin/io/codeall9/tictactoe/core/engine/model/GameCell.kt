package io.codeall9.tictactoe.core.engine.model

public sealed class Cell {
    public data class Marked(val player: Player) : Cell()
    public object Empty : Cell()

    public companion object {
        public val O: Marked = Marked(Player.O)
        public val X: Marked = Marked(Player.X)
    }
}

public enum class CellPosition {
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