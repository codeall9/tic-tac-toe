package io.codeall9.tictactoe.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.codeall9.engine.model.Cell
import io.codeall9.engine.model.CellPosition
import io.codeall9.tictactoe.game.Box
import io.codeall9.tictactoe.theme.TicTacToeTheme


@Composable
fun GameBoard(
    modifier: Modifier = Modifier,
    boxes: List<Box>,
    onCellClick: (CellPosition) -> Unit = { /* no-op */ },
) {
    val items = boxes.chunked(3)
    BoxWithConstraints(modifier = modifier) {
        val cellWith = maxWidth / 3
        Column(modifier = modifier) {
            for (chunkedBoxes in items) {
                GameBoxRow(
                    items = chunkedBoxes,
                    cellWith = cellWith,
                    onCellClick = onCellClick,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun GameBoxRow(
    modifier: Modifier = Modifier,
    items: List<Box>,
    cellWith: Dp,
    onCellClick: (CellPosition) -> Unit = { /* no-op */ },
) {
    Row(
        modifier = modifier
            .wrapContentHeight()
    ) {
        for ((position, cell) in items) {
            val mark = cell.let { it as? Cell.Marked }
                ?.run { player.name }
                ?: " "
            GameBoxButton(
                Modifier
                    .width(cellWith)
                    .padding(8.dp)
                    .aspectRatio(1.0f),
                mark = mark,
                onClick = { onCellClick(position) }
            )
        }
    }
}

@Composable
private fun GameBoxButton(
    modifier: Modifier = Modifier,
    mark: String,
    onClick: () -> Unit = { /* no-op */ },
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        enabled = mark.isBlank(),
        colors = ButtonDefaults.outlinedButtonColors(disabledContentColor = MaterialTheme.colors.primary)
    ) {
        Text(
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
            text = mark,
        )
    }
}

@Preview
@Composable
private fun GameBoardPreview() {
    val data = listOf(
        Box(CellPosition.TopStart, Cell.X), Box(CellPosition.TopCenter, Cell.O), Box(CellPosition.TopEnd, Cell.X),
        Box(CellPosition.CenterStart, Cell.Empty), Box(CellPosition.Center, Cell.O), Box(
            CellPosition.CenterEnd, Cell.Empty),
        Box(CellPosition.BottomStart, Cell.Empty), Box(CellPosition.BottomCenter, Cell.Empty), Box(
            CellPosition.BottomEnd, Cell.O),
    )
    TicTacToeTheme {
        Surface {
            GameBoard(
                boxes = data,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
    }
}