package io.codeall9.history.repository

import io.codeall9.history.GameLog
import io.codeall9.history.GetGameHistory
import io.codeall9.history.LogGameTransition
import io.codeall9.history.persistence.GameEvent
import io.codeall9.history.persistence.GameEventDao
import io.codeall9.history.services.LogNonFatalException
import io.codeall9.history.transform.GameEventToGameLog
import io.codeall9.history.transform.GameLogToEvent
import io.codeall9.history.transform.toGameEvent
import io.codeall9.history.transform.toGameLog


internal inline fun GameEventDao.appendLog(
    crossinline toEntity: GameLogToEvent = GameLog::toGameEvent,
): LogGameTransition = { log ->
    insert(log.toEntity())
}

internal inline fun GameEventDao.queryGameLogs(
    crossinline toDomain: GameEventToGameLog = GameEvent::toGameLog,
    crossinline onError: LogNonFatalException = {},
): GetGameHistory = { id ->
    runCatching { getGameHistory(id.value) }
        .onFailure(onError)
        .getOrElse { emptyList() }
        .asSequence()
        .map { event -> event.runCatching(toDomain).onFailure(onError) }
        .mapNotNull { result -> result.getOrNull() }
        .toList()
}