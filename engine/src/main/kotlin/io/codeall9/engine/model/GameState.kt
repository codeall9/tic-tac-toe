package io.codeall9.engine.model

typealias Markable = suspend () -> GameState
typealias ValidRounds = Map<CellPosition, Markable>

sealed class GameState(val board: Board)

class PlayerOTurn(current: Board, val actions: ValidRounds) : GameState(current)
class PlayerXTurn(current: Board, val actions: ValidRounds) : GameState(current)
class GameWon(board: Board, val winner: Player) : GameState(board)
class GameTie(board: Board) : GameState(board)
