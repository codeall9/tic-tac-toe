package io.codeall9.history

import io.codeall9.engine.model.Player
import kotlinx.coroutines.flow.Flow

public typealias GetRecentPlayedGames = (total: UInt) -> Flow<List<PlayedGame>>
public typealias GetGameResult = suspend (id: GameId) -> PlayedGame
public typealias AddPlayedGame = suspend (PlayedGame) -> Unit

public sealed class PlayedGame(public val gameId: GameId)

public data class GameTied(private val id: GameId): PlayedGame(id)

public data class PlayerWon(
    private val id: GameId,
    public val winner: Player,
): PlayedGame(id)