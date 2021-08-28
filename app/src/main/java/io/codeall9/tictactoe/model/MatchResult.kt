package io.codeall9.tictactoe.model

typealias Markable = suspend () -> MatchResult
typealias ValidRounds = Map<CellPosition, Markable>

sealed class MatchResult(val board: Board)

class PlayerOTurn(current: Board, val actions: ValidRounds) : MatchResult(current)
class PlayerXTurn(current: Board, val actions: ValidRounds) : MatchResult(current)
class GameOver(board: Board, val winner: Player) : MatchResult(board)
class GameTie(board: Board) : MatchResult(board)
