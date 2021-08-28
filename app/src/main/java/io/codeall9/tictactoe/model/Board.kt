package io.codeall9.tictactoe.model

@JvmInline
value class Board constructor(private val grid: Map<CellPosition, Cell>) {

    constructor() : this(AllPositions.associateWith { Cell.Empty })

    val topStart get() = grid[CellPosition.TopStart]!!
    val topCenter get() = grid[CellPosition.TopCenter]!!
    val topEnd get() = grid[CellPosition.TopEnd]!!
    val centerStart get() = grid[CellPosition.CenterStart]!!
    val center get() = grid[CellPosition.Center]!!
    val centerEnd get() = grid[CellPosition.CenterEnd]!!
    val bottomStart get() = grid[CellPosition.BottomStart]!!
    val bottomCenter get() = grid[CellPosition.BottomCenter]!!
    val bottomEnd get() = grid[CellPosition.BottomEnd]!!

    init {
        require(grid.size == 9) { "expected size: 9, actual size: ${grid.size}" }
        for (position in AllPositions) {
            requireNotNull(grid[position]) { "missing cell at $position" }
        }
    }

    operator fun get(position: CellPosition): Cell {
        return grid[position]!!
    }

    fun isAllMarked() = grid.all { (_, cell) -> cell is Cell.Marked }

    companion object {
        val AllPositions = listOf(
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

        val Empty = Board()

        @JvmStatic
        operator fun invoke(
            topStart: Cell = Cell.Empty,
            topCenter: Cell = Cell.Empty,
            topEnd: Cell = Cell.Empty,
            centerStart: Cell = Cell.Empty,
            center: Cell = Cell.Empty,
            centerEnd: Cell = Cell.Empty,
            bottomStart: Cell = Cell.Empty,
            bottomCenter: Cell = Cell.Empty,
            bottomEnd: Cell = Cell.Empty,
        ) : Board {
            val initMap = mapOf(
                CellPosition.TopStart to topStart,
                CellPosition.TopCenter to topCenter,
                CellPosition.TopEnd to topEnd,
                CellPosition.CenterStart to centerStart,
                CellPosition.Center to center,
                CellPosition.CenterEnd to centerEnd,
                CellPosition.BottomStart to bottomStart,
                CellPosition.BottomCenter to bottomCenter,
                CellPosition.BottomEnd to bottomEnd,
            )
            return Board(initMap)
        }
    }
}