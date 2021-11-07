package io.codeall9.history.transform

import io.codeall9.history.GameLog
import io.codeall9.history.GameStarted
import io.codeall9.history.MovePlayed
import io.codeall9.history.persistence.GameEvent

internal typealias GameLogToEvent = GameLog.() -> GameEvent

internal fun GameLog.toGameEvent(): GameEvent {
    return when (this) {
        is GameStarted -> toStartedEvent()
        is MovePlayed -> toMovePlayedEvent()
    }
}

private fun GameStarted.toStartedEvent(): GameEvent {
    return GameEvent(
        gameId.value,
        GameEvent.EventType.STARTED,
        first,
        null,
    )
}

private fun MovePlayed.toMovePlayedEvent(): GameEvent {
    return GameEvent(
        gameId.value,
        GameEvent.EventType.MOVE_PLAYED,
        player,
        position,
    )
}
