package io.codeall9.tictactoe.infra.history.repository

import io.codeall9.core.history.event.GameResult
import io.codeall9.core.history.event.GameTransition
import io.codeall9.core.history.model.GameHistory
import io.codeall9.core.history.repository.GetGameHistory
import io.codeall9.core.history.repository.GetRecentGameResult
import io.codeall9.core.history.repository.LogGameTransition
import io.codeall9.tictactoe.infra.history.comman.HandleException
import io.codeall9.tictactoe.infra.history.comman.Transform
import io.codeall9.tictactoe.infra.history.database.GameEvent
import io.codeall9.tictactoe.infra.history.database.GameEventDao
import io.codeall9.tictactoe.infra.history.transform.toGameEvent
import io.codeall9.tictactoe.infra.history.transform.toGameTransition
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion

internal inline fun LogGameTransition(
    dao: GameEventDao,
    handleError: HandleException = HandleException {},
    crossinline toGameEvent: Transform<GameTransition, GameEvent> = GameTransition::toGameEvent,
): LogGameTransition {
    return LogGameTransition { transition ->
        dao.runCatching { insert(transition.toGameEvent()) }
            .onFailure(handleError)
            .isSuccess
    }
}

internal inline fun GetGameHistory(
    dao: GameEventDao,
    handleError: HandleException = HandleException {},
    crossinline toGameTransition: Transform<GameEvent, GameTransition> = GameEvent::toGameTransition,
): GetGameHistory {
    return GetGameHistory { id ->
        dao.runCatching { getGameHistory(id.value) }
            .mapCatching { events -> events.map(toGameTransition) }
            .mapCatching { GameHistory.replay(it) }
            .onFailure(handleError)
            .getOrNull()
    }
}

internal inline fun GetRecentGameResult(
    dao: GameEventDao,
    handleError: HandleException = HandleException {},
    crossinline toGameTransition: Transform<GameEvent, GameTransition> = GameEvent::toGameTransition,
): GetRecentGameResult {
    return GetRecentGameResult { total ->
        dao.runCatching { getRecentResults(total.toInt()) }
            .onFailure(handleError)
            .getOrElse { flowOf(emptyList()) }
            .map { list ->
                list.mapNotNull { result ->
                    result.runCatching(toGameTransition)
                        .mapCatching { it as GameResult }
                        .onFailure(handleError)
                        .getOrNull()
                }
            }
            .onCompletion { it?.run(handleError) }
            .catch { emit(emptyList()) }
    }
}
