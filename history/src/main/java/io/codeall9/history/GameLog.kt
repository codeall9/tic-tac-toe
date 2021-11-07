package io.codeall9.history

import io.codeall9.engine.model.CellPosition
import io.codeall9.engine.model.Player


public typealias LogGameTransition = suspend (GameLog) -> Unit
public typealias GetGameHistory = suspend (GameId) -> List<GameLog>

public sealed class GameLog(internal val gameId: GameId)

public data class GameStarted(
    private val id: GameId,
    public val first: Player,
): GameLog(id)

public data class MovePlayed(
    private val id: GameId,
    public val player: Player,
    public val position: CellPosition,
): GameLog(id)