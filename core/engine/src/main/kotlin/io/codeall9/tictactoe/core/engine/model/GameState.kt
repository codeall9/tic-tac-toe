package io.codeall9.tictactoe.core.engine.model

public typealias Markable = suspend () -> GameState
public typealias ValidRounds = Map<CellPosition, Markable>

public sealed class GameState(public val board: Board)

public class PlayerOTurn(current: Board, public val actions: ValidRounds) : GameState(current)
public class PlayerXTurn(current: Board, public val actions: ValidRounds) : GameState(current)
public class GameWon(board: Board, public val winner: Player) : GameState(board)
public class GameTie(board: Board) : GameState(board)

public suspend fun PlayerOTurn.markOrNull(position: CellPosition): GameState? {
    return runCatching { requireNotNull(actions[position]) }
        .mapCatching { mark -> mark() }
        .getOrNull()
}

public suspend fun PlayerXTurn.markOrNull(position: CellPosition): GameState? {
    return runCatching { requireNotNull(actions[position]) }
        .mapCatching { mark -> mark() }
        .getOrNull()
}

public fun GameState.isOnGoing(): Boolean {
    return this is PlayerOTurn || this is PlayerXTurn
}
public fun GameState.isEnded(): Boolean {
    return this is GameWon || this is GameTie
}
