package io.codeall9.tictactoe.infra.history.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

internal class SimpleGameEventDao(
    private val state: MutableStateFlow<MutableList<GameEvent>> = MutableStateFlow(mutableListOf()),
) : GameEventDao {

    val raw: MutableList<GameEvent> get() = state.value

    var isAccessDenied: Boolean = false

    override suspend fun getGameEvent(eventId: Long): GameEvent = throwIf(::isAccessDenied) {
        return raw.first { it.eventId == eventId }
    }

    override suspend fun getGameHistory(historyId: String): List<GameEvent> = throwIf(::isAccessDenied) {
        return raw
            .asSequence()
            .filter { it.historyId == historyId }
            .sortedBy { it.createdAt.plusNanos(it.eventId) }
            .toList()
    }

    override fun getRecentResults(total: Int): Flow<List<GameEvent>> = throwIf(::isAccessDenied) {
        return state.map { list ->
            list.asSequence()
                .filter { it.eventType == GameEvent.EventType.ENDED }
                .sortedByDescending { it.createdAt.plusNanos(it.eventId) }
                .take(total)
                .toList()
        }
    }

    override fun getGameResult(historyId: String): Flow<GameEvent> = throwIf(::isAccessDenied) {
        return state.map { list ->
            list.asSequence()
                .filter { it.eventType == GameEvent.EventType.ENDED }
                .first { it.historyId == historyId }
        }
    }

    override suspend fun insert(entity: GameEvent): Long = throwIf(::isAccessDenied) {
        val eventId = state.value.size.toLong()

        state.update {
            it.apply { add(entity.copy(eventId = eventId)) }
        }

        return eventId
    }

    override suspend fun insert(vararg entities: GameEvent): List<Long> = throwIf(::isAccessDenied) {
        return entities.map { insert(it) }
    }

    private inline fun <T> throwIf(predicate: () -> Boolean, block: () -> T): T {
        if (predicate()) error("expected to throw exception")

        return block()
    }
}