package io.codeall9.tictactoe.infra.history.transform

import io.codeall9.core.history.event.GameOver
import io.codeall9.core.history.event.GameStarted
import io.codeall9.core.history.event.GameTied
import io.codeall9.core.history.event.GameTransition
import io.codeall9.core.history.event.MovePlayed
import io.codeall9.core.history.event.PlayerWon
import io.codeall9.core.history.model.HistoryId
import io.codeall9.tictactoe.infra.history.database.GameEvent
import java.util.UUID

@Throws(IllegalStateException::class)
internal fun GameEvent.toGameTransition(): GameTransition {
    return when (eventType) {
        GameEvent.EventType.STARTED -> toGameStarted()
        GameEvent.EventType.MOVE_PLAYED -> toMovePlayed()
        GameEvent.EventType.ENDED -> toGameOver()
    }
}

@Throws(IllegalStateException::class)
private fun GameEvent.toGameStarted(): GameStarted {
    checkNotNull(player) { "player was null." }
    val uuid = runCatching { UUID.fromString(historyId) }
        .getOrElse { error("$historyId is invalid uuid") }
    return GameStarted(
        HistoryId.of(uuid),
        player,
    )
}

@Throws(IllegalStateException::class)
private fun GameEvent.toMovePlayed(): MovePlayed {
    checkNotNull(player) { "player was null." }
    checkNotNull(position) { "position was null." }
    val uuid = runCatching { UUID.fromString(historyId) }
        .getOrElse { error("$historyId is invalid uuid") }
    return MovePlayed(
        HistoryId.of(uuid),
        player,
        position,
    )
}

@Throws(IllegalStateException::class)
private fun GameEvent.toGameOver(): GameOver {
    val uuid = runCatching { UUID.fromString(historyId) }
        .getOrElse { error("$historyId is invalid uuid") }
    val winner = player ?: return GameTied(HistoryId.of(uuid))

    return PlayerWon(HistoryId.of(uuid), winner)
}