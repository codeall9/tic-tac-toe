package io.codeall9.history.transform

import io.codeall9.history.GameId
import io.codeall9.history.GameLog
import io.codeall9.history.GameStarted
import io.codeall9.history.MovePlayed
import io.codeall9.history.persistence.GameEvent
import io.codeall9.history.persistence.GameEvent.EventType.MOVE_PLAYED
import io.codeall9.history.persistence.GameEvent.EventType.STARTED

internal typealias GameEventToGameLog = GameEvent.() -> GameLog

@Throws(IllegalStateException::class)
internal fun GameEvent.toGameLog(): GameLog {
    return when (eventType) {
        STARTED -> toGameStarted()
        MOVE_PLAYED -> toMovePlayed()
    }
}

@Throws(IllegalStateException::class)
private fun GameEvent.toGameStarted(): GameStarted {
    checkNotNull(player) { "player was null." }
    return GameStarted(
        GameId(gameId),
        player,
    )
}

@Throws(IllegalStateException::class)
private fun GameEvent.toMovePlayed(): MovePlayed {
    checkNotNull(player) { "player was null." }
    checkNotNull(position) { "position was null." }
    return MovePlayed(
        GameId(gameId),
        player,
        position,
    )
}
