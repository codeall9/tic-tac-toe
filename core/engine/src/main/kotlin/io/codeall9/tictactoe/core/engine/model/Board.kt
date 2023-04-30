package io.codeall9.tictactoe.core.engine.model

@JvmInline
public value class Board internal constructor(
    private val grid: Map<CellPosition, Cell>,
): Map<CellPosition, Cell> by grid {

    public constructor() : this(AllPositions.associateWith { Cell.Empty })

    init {
        require(grid.size == 9) { "expected size: 9, actual size: ${grid.size}" }
    }

    public fun copy(
        topStart: Cell = this[CellPosition.TopStart],
        topCenter: Cell = this[CellPosition.TopCenter],
        topEnd: Cell = this[CellPosition.TopEnd],
        centerStart: Cell = this[CellPosition.CenterStart],
        center: Cell = this[CellPosition.Center],
        centerEnd: Cell = this[CellPosition.CenterEnd],
        bottomStart: Cell = this[CellPosition.BottomStart],
        bottomCenter: Cell = this[CellPosition.BottomCenter],
        bottomEnd: Cell = this[CellPosition.BottomEnd],
    ): Board {
        return Board(topStart, topCenter, topEnd, centerStart, center, centerEnd, bottomStart, bottomCenter, bottomEnd)
    }

    public fun isAllPositionEquals(other: Board?): Boolean {
        return grid.all { other?.get(it.key) == it.value }
    }

    public override operator fun get(key: CellPosition): Cell {
        return requireNotNull(grid[key])
    }

    public fun isAllMarked(): Boolean = grid.all { (_, cell) -> cell is Cell.Marked }

    public fun mark(position: CellPosition, cell: Cell): Board {
        return when (position) {
            CellPosition.TopStart -> copy(topStart = cell)
            CellPosition.TopCenter -> copy(topCenter = cell)
            CellPosition.TopEnd -> copy(topEnd = cell)
            CellPosition.CenterStart -> copy(centerStart = cell)
            CellPosition.Center -> copy(center = cell)
            CellPosition.CenterEnd -> copy(centerEnd = cell)
            CellPosition.BottomStart -> copy(bottomStart = cell)
            CellPosition.BottomCenter -> copy(bottomCenter = cell)
            CellPosition.BottomEnd -> copy(bottomEnd = cell)
        }
    }

    public companion object {
        public val AllPositions: List<CellPosition> = listOf(
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

        public val Empty: Board = Board()

        @JvmStatic
        public operator fun invoke(
            topStart: Cell = Cell.Empty,
            topCenter: Cell = Cell.Empty,
            topEnd: Cell = Cell.Empty,
            centerStart: Cell = Cell.Empty,
            center: Cell = Cell.Empty,
            centerEnd: Cell = Cell.Empty,
            bottomStart: Cell = Cell.Empty,
            bottomCenter: Cell = Cell.Empty,
            bottomEnd: Cell = Cell.Empty,
        ): Board {
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