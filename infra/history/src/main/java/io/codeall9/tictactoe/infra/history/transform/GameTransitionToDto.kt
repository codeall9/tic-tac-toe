package io.codeall9.tictactoe.infra.history.transform

import io.codeall9.core.history.event.GameStarted
import io.codeall9.core.history.event.GameTied
import io.codeall9.core.history.event.GameTransition
import io.codeall9.core.history.event.MovePlayed
import io.codeall9.core.history.event.PlayerWon
import io.codeall9.core.history.model.HistoryId
import io.codeall9.tictactoe.core.engine.model.CellPosition
import io.codeall9.tictactoe.core.engine.model.Player
import io.codeall9.tictactoe.infra.history.database.GameEvent


internal fun GameTransition.toGameEvent(): GameEvent {
    return when (this) {
        is GameTied -> toDto(historyId)
        is PlayerWon -> toDto(historyId, winner)
        is GameStarted -> toDto(historyId, first)
        is MovePlayed -> toDto(historyId, player, position)
    }
}

private fun GameStarted.toDto(id: HistoryId, first: Player): GameEvent {
    return GameEvent(
        eventType = GameEvent.EventType.STARTED,
        historyId = id.value,
        player = first,
        position = null,
    )
}

private fun MovePlayed.toDto(
    id: HistoryId,
    player: Player,
    position: CellPosition,
): GameEvent {
    return GameEvent(
        eventType = GameEvent.EventType.MOVE_PLAYED,
        historyId = id.value,
        player = player,
        position = position,
    )
}

private fun GameTied.toDto(id: HistoryId): GameEvent {
    return GameEvent(
        eventType = GameEvent.EventType.ENDED,
        historyId = id.value,
        player = null,
        position = null,
    )
}

private fun PlayerWon.toDto(id: HistoryId, winner: Player): GameEvent {
    return GameEvent(
        eventType = GameEvent.EventType.ENDED,
        historyId = id.value,
        player = winner,
        position = null,
    )
}
