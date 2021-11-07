package io.codeall9.history.persistance

import io.codeall9.history.persistence.GameEvent
import io.codeall9.history.persistence.GameEventDao

internal abstract class MockedGameEventDao: GameEventDao {
    private var eventId: Long = 0
    private val table = mutableMapOf<Long, GameEvent>()

    override suspend fun getGameEvent(eventId: Long): GameEvent {
        return table[eventId] ?: throw NoSuchElementException("No element of $eventId")
    }

    override suspend fun getGameHistory(gameId: String): List<GameEvent> {
        return table.values
            .filter { it.gameId == gameId }
    }

    override suspend fun insert(entity: GameEvent): Long {
        check(table[eventId] == null) { "id: $eventId is duplicated" }
        table[eventId] = entity.copy(eventId = eventId)
        return eventId++
    }

    override suspend fun insert(vararg entities: GameEvent): List<Long> {
        return entities.map { insert(it) }
    }
}