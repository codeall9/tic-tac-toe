package io.codeall9.core.history.repository

import io.codeall9.core.history.event.GameResult
import io.codeall9.core.history.event.GameTransition
import io.codeall9.core.history.model.GameHistory
import io.codeall9.core.history.model.HistoryId
import kotlinx.coroutines.flow.Flow

public fun interface LogGameTransition: suspend (GameTransition) -> Boolean

public fun interface GetGameHistory: suspend (HistoryId) -> GameHistory?

public fun interface GetRecentGameResult: (UInt) -> Flow<List<GameResult>>