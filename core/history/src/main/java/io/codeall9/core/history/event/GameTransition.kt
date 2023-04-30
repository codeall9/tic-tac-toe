package io.codeall9.core.history.event

import io.codeall9.core.history.model.HistoryId
import io.codeall9.tictactoe.core.engine.model.CellPosition
import io.codeall9.tictactoe.core.engine.model.Player

public sealed class GameTransition(public val historyId: HistoryId)

public data class GameStarted(
    private val id: HistoryId,
    public val first: Player,
): GameTransition(id)

public data class MovePlayed(
    private val id: HistoryId,
    public val player: Player,
    public val position: CellPosition,
): GameTransition(id)

public sealed class GameOver(id: HistoryId): GameTransition(id)

public data class GameTied(private val id: HistoryId): GameOver(id)

public data class PlayerWon(
    private val id: HistoryId,
    val winner: Player
): GameOver(id)

public typealias GameResult = GameOver