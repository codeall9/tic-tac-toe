package io.codeall9.core.history.model

import io.codeall9.core.history.event.GameOver
import io.codeall9.core.history.event.GameStarted
import io.codeall9.core.history.event.GameTransition
import io.codeall9.core.history.event.MovePlayed
import io.codeall9.tictactoe.core.engine.model.Board
import io.codeall9.tictactoe.core.engine.model.Cell
import io.codeall9.tictactoe.core.engine.model.Player
import kotlin.LazyThreadSafetyMode.PUBLICATION

/**
 * Represents a sequence of game board snapshots for a single game. The history of the game can be replayed
 * by iterating through the sequence of board states in order. The class is immutable and can only be created
 * through the `replay` or `replayOrNull` methods.
 *
 * @param snapshots A list of lazy board snapshots representing the sequence of board states in the game history
 */
@JvmInline
public value class GameHistory internal constructor(
    private val snapshots: List<Lazy<Board>>,
): Iterable<Board> {

    /** Returns the total number of rounds played in the game */
    public val totalRounds: Int get() = snapshots.size - 1

    override fun iterator(): Iterator<Board> {
        return snapshots.asSequence()
            .map { it.value }
            .iterator()
    }

    /**
     * Returns the board state at the given round number, or null if the round number is out of range
     *
     * @param round The round number (1-indexed) of the board state to retrieve
     * @return The board state at the given round number, or null if the round number is out of range
     */
    public operator fun get(round: Int): Board? {
        return snapshots
            .getOrNull(round - 1)
            ?.value
    }

    public companion object {

        /**
         * Attempts to replay a sequence of game events and returns a `GameHistory` object representing the game
         * history if the replay was successful, or null if the replay failed.
         *
         * @param events The list of game events to replay
         * @return A `GameHistory` object representing the game history, or null if the replay failed
         */
        @JvmStatic
        public fun replayOrNull(events: List<GameTransition>): GameHistory? {
            return runCatching { replay(events) }.getOrNull()
        }

        /**
         * Replays a sequence of game events and returns a `GameHistory` object representing the game history.
         *
         * @param events The list of game events to replay
         * @return A `GameHistory` object representing the game history
         * @throws IllegalArgumentException If the list of events is not a valid sequence
         */
        @Throws(IllegalArgumentException::class)
        @JvmStatic
        public fun replay(events: List<GameTransition>): GameHistory {
            require(events.indexOfLast { it is GameStarted } == 0) {
                "Events should contain only one `GameStarted` at the beginning"
            }
            require(events.indexOfFirst { it is GameOver } == events.lastIndex) {
                "Events should contain only one `GameOver` at the end"
            }
            return events.dropLast(1)
                .indices
                .map { round -> boardLazy(round, events) }
                .let { GameHistory(it) }
        }
    }
}

private val Player.signature: Cell
    get() = when (this) {
        Player.O -> Cell.O
        Player.X -> Cell.X
    }

private fun Board.applyEvent(event: GameTransition): Board = when (event) {
    is MovePlayed -> {
        event.run { mark(position, player.signature) }
    }
    else -> this
}

private fun boardLazy(total: Int, events: List<GameTransition>) = lazy(PUBLICATION) {
    (0..total)
        .fold(Board.Empty) { acc, i ->
            acc.applyEvent(events[i])
        }
}
