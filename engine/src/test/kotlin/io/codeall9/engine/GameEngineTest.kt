package io.codeall9.engine

import io.codeall9.engine.model.*
import io.codeall9.engine.model.CellPosition.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class GameEngineTest {

    val newGame: TicTacToeInitializer = initLocalGame

    @Nested
    @DisplayName("Given User is Playing")
    inner class GamePlay {

        @ParameterizedTest
        @EnumSource(Player::class)
        @DisplayName("When Player move, then the game switch to opponent turn")
        fun playerMove(player: Player) = runBlockingTest {
            val result = runCatching { newGame(player) }
                .mapCatching { player -> player.mark(BottomCenter) }
                .getOrElse { fail(it) }

            when (player) {
                Player.O -> assertIs<PlayerXTurn>(result)
                Player.X -> assertIs<PlayerOTurn>(result)
            }
        }

        @Test
        @DisplayName("When Player turn, then player can't take another move in same turn")
        fun invalidMove() = runBlockingTest {
            val validRounds = runCatching { newGame(Player.X) }
                .mapCatching { it as PlayerXTurn }
                .mapCatching { turn -> turn.actions }
                .getOrElse { fail(it) }

            val playTwice = runCatching { validRounds }
                .mapCatching { move ->
                    listOf(TopEnd, Center, BottomStart)
                        .asSequence()
                        .map { position -> requireNotNull(move[position]) }
                        .map { mark ->
                            async { mark() }
                        }
                        .toList()
                }
                .mapCatching { it.awaitAll() }

            assertThrows<IllegalStateException> { playTwice.getOrThrow() }
        }
    }

    @Nested
    @DisplayName("Given End Game Condition")
    inner class EndGameCondition {

        @Test
        @DisplayName("When all cells in the Top Row belong to O, then O win")
        fun topRow() = runBlockingTest {
            val playerOMoves = listOf(TopEnd, TopStart, TopCenter)
            val playerXMoves = listOf(CenterEnd, BottomEnd)
            val finalBoard = Board(playerO = playerOMoves, playerX = playerXMoves)
            val expected = GameWon(finalBoard, Player.O)

            val newGame = runCatching { newGame(Player.O) }

            buildSteps(playerOMoves, playerXMoves)
                .fold(newGame) { current, position ->
                    current.mapCatching { it.mark(position) }
                }
                .mapCatching { it as GameWon }
                .getOrElse { fail(it) }
                .run {
                    assertEquals(expected.winner, winner)
                    assertEquals(expected.board, board)
                }
        }

        @Test
        @DisplayName("When all cells in the Center Row belong to O, then O win")
        fun centerRow() = runBlockingTest {
            val playerOMoves = listOf(Center, CenterStart, CenterEnd)
            val playerXMoves = listOf(TopCenter, BottomEnd)
            val finalBoard = Board(playerO = playerOMoves, playerX = playerXMoves)
            val expected = GameWon(finalBoard, Player.O)

            val newGame = runCatching { newGame(Player.O) }

            buildSteps(playerOMoves, playerXMoves)
                .fold(newGame) { current, position ->
                    current.mapCatching { it.mark(position) }
                }
                .mapCatching { it as GameWon }
                .getOrElse { fail(it) }
                .run {
                    assertEquals(expected.winner, winner)
                    assertEquals(expected.board, board)
                }
        }

        @Test
        @DisplayName("When all cells in the Bottom Row belong to O, then O win")
        fun bottomRow() = runBlockingTest {
            val playerOMoves = listOf(BottomStart, BottomCenter, BottomEnd)
            val playerXMoves = listOf(Center, CenterStart)
            val finalBoard = Board(playerO = playerOMoves, playerX = playerXMoves)
            val expected = GameWon(finalBoard, Player.O)

            val newGame = runCatching { newGame(Player.O) }

            buildSteps(playerOMoves, playerXMoves)
                .fold(newGame) { current, position ->
                    current.mapCatching { it.mark(position) }
                }
                .mapCatching { it as GameWon }
                .getOrElse { fail(it) }
                .run {
                    assertEquals(expected.winner, winner)
                    assertEquals(expected.board, board)
                }
        }

        @Test
        @DisplayName("When all cells in the Start Column belong to X, then X win")
        fun startColumn() = runBlockingTest {
            val playerOMoves = listOf(Center, TopEnd, TopCenter)
            val playerXMoves = listOf(TopStart, BottomStart, CenterStart)
            val finalBoard = Board(playerO = playerOMoves, playerX = playerXMoves)
            val expected = GameWon(finalBoard, Player.X)

            val newGame = runCatching { newGame(Player.O) }

            buildSteps(playerOMoves, playerXMoves)
                .fold(newGame) { current, position ->
                    current.mapCatching { it.mark(position) }
                }
                .mapCatching { it as GameWon }
                .getOrElse { fail(it) }
                .run {
                    assertEquals(expected.winner, winner)
                    assertEquals(expected.board, board)
                }
        }

        @Test
        @DisplayName("When all cells in the Center Column belong to X, then X win")
        fun centerColumn() = runBlockingTest {
            val playerOMoves = listOf(BottomEnd, TopStart, CenterStart)
            val playerXMoves = listOf(TopCenter, Center, BottomCenter)
            val finalBoard = Board(playerO = playerOMoves, playerX = playerXMoves)
            val expected = GameWon(finalBoard, Player.X)

            val newGame = runCatching { newGame(Player.O) }

            buildSteps(playerOMoves, playerXMoves)
                .fold(newGame) { current, position ->
                    current.mapCatching { it.mark(position) }
                }
                .mapCatching { it as GameWon }
                .getOrElse { fail(it) }
                .run {
                    assertEquals(expected.winner, winner)
                    assertEquals(expected.board, board)
                }
        }

        @Test
        @DisplayName("When all cells in the End Column belong to X, then X win")
        fun endColumn() = runBlockingTest {
            val playerOMoves = listOf(Center, BottomStart, CenterStart)
            val playerXMoves = listOf(BottomEnd, TopEnd, CenterEnd)
            val finalBoard = Board(playerO = playerOMoves, playerX = playerXMoves)
            val expected = GameWon(finalBoard, Player.X)

            val newGame = runCatching { newGame(Player.O) }

            buildSteps(playerOMoves, playerXMoves)
                .fold(newGame) { current, position ->
                    current.mapCatching { it.mark(position) }
                }
                .mapCatching { it as GameWon }
                .getOrElse { fail(it) }
                .run {
                    assertEquals(expected.winner, winner)
                    assertEquals(expected.board, board)
                }
        }

        @Test
        @DisplayName("When all cells in the Main Diagonal, then Player O win")
        fun mainDiagonal() = runBlockingTest {
            val playerOMoves = listOf(Center, BottomEnd, TopStart)
            val playerXMoves = listOf(CenterStart, BottomStart)
            val finalBoard = Board(playerO = playerOMoves, playerX = playerXMoves)
            val expected = GameWon(finalBoard, Player.O)

            val newGame = runCatching { newGame(Player.O) }

            buildSteps(playerOMoves, playerXMoves)
                .fold(newGame) { current, position ->
                    current.mapCatching { it.mark(position) }
                }
                .mapCatching { it as GameWon }
                .getOrElse { fail(it) }
                .run {
                    assertEquals(expected.winner, winner)
                    assertEquals(expected.board, board)
                }
        }

        @Test
        @DisplayName("When all cells in anti-diagonal belong to X, then X win")
        fun antiDiagonal() = runBlockingTest {
            val playerOMoves = listOf(TopCenter, TopStart)
            val playerXMoves = listOf(Center, BottomStart, TopEnd)
            val finalBoard = Board(playerO = playerOMoves, playerX = playerXMoves)
            val expected = GameWon(finalBoard, Player.X)

            val newGame = runCatching { newGame(Player.X) }

            buildSteps(playerXMoves, playerOMoves)
                .fold(newGame) { current, position ->
                    current.mapCatching { it.mark(position) }
                }
                .mapCatching { it as GameWon }
                .getOrElse { fail(it) }
                .run {
                    assertEquals(expected.winner, winner)
                    assertEquals(expected.board, board)
                }
        }

        @Test
        @DisplayName("When all cells are Marked and there is no winner, then Game Tie")
        fun boardFull() = runBlockingTest {
            val playerOMoves = listOf(TopCenter, TopEnd, BottomEnd, CenterStart, BottomStart)
            val playerXMoves = listOf(Center, TopStart, CenterEnd, BottomCenter)
            val finalBoard = Board(playerO = playerOMoves, playerX = playerXMoves)
            val expected = GameTie(finalBoard)

            val newGame = runCatching { newGame(Player.O) }
            buildSteps(playerOMoves, playerXMoves)
                .fold(newGame) { current, position ->
                    current.mapCatching { it.mark(position) }
                }
                .mapCatching { it as GameTie }
                .getOrElse { fail(it) }
                .run {
                    Board.AllPositions.forEach {
                        assertEquals(expected.board[it], board[it])
                    }
                }
        }
    }

    private suspend fun GameState.mark(position: CellPosition): GameState {
        return when (this) {
            is GameWon -> throw IllegalStateException("GameOver")
            is GameTie -> throw IllegalStateException("GameTie")
            is PlayerOTurn -> requireNotNull(actions[position], { "$position not found" }).invoke()
            is PlayerXTurn -> requireNotNull(actions[position], { "$position not found" }).invoke()
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun buildSteps(
        firstPos: List<CellPosition>,
        secondPos: List<CellPosition>,
    ): List<CellPosition> {
        val minSize = minOf(firstPos.size, secondPos.size)
        val sublist = when {
            firstPos.size > secondPos.size -> {
                firstPos.subList(secondPos.size, firstPos.size)
            }
            firstPos.size < secondPos.size -> {
                secondPos.subList(firstPos.size, secondPos.size)
            }
            else -> null
        }
        return buildList(firstPos.size + secondPos.size) {
            repeat(minSize) { index ->
                add(firstPos[index])
                add(secondPos[index])
            }
            if (sublist != null) {
                addAll(sublist)
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private operator fun Board.Companion.invoke(
        playerO: List<CellPosition>,
        playerX: List<CellPosition>,
    ): Board {
        val map = buildMap<CellPosition, Cell>(9) {
            playerO.asSequence()
                .map { it to Cell.O }
                .plus(playerX.asSequence().map { it to Cell.X })
                .distinctBy { it.first }
                .forEach { (key, value) -> put(key, value) }
            AllPositions
                .asSequence()
                .filter { get(it) == null }
                .forEach { putIfAbsent(it, Cell.Empty) }
        }
        return Board(map)
    }
}
