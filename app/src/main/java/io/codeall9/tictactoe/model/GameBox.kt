package io.codeall9.tictactoe.model

import io.codeall9.tictactoe.core.engine.model.Board
import io.codeall9.tictactoe.core.engine.model.Cell
import io.codeall9.tictactoe.core.engine.model.CellPosition

private val CellOrders = listOf(
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

data class GameBox(val position: CellPosition, val cell: Cell)

fun Board.toBoxList(): List<GameBox> {
    return CellOrders.map { position ->  GameBox(position, this[position]) }
}