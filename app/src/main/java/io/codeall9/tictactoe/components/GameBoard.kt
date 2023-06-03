package io.codeall9.tictactoe.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.codeall9.tictactoe.core.engine.model.Cell
import io.codeall9.tictactoe.core.engine.model.CellPosition
import io.codeall9.tictactoe.model.GameBox
import io.codeall9.tictactoe.theme.TicTacToeTheme


@Composable
fun GameBoard(
    modifier: Modifier = Modifier,
    boxes: List<GameBox>,
    onCellClick: (CellPosition) -> Unit = { /* no-op */ },
) {
    val items = boxes.chunked(3)
    BoxWithConstraints(modifier = modifier) {
        val cellWith = maxWidth / 3
        Column(modifier = Modifier.fillMaxWidth()) {
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
    items: List<GameBox>,
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
        GameBox(CellPosition.TopStart, Cell.X),
        GameBox(CellPosition.TopCenter, Cell.O),
        GameBox(CellPosition.TopEnd, Cell.X),
        GameBox(CellPosition.CenterStart, Cell.Empty),
        GameBox(CellPosition.Center, Cell.O),
        GameBox(CellPosition.CenterEnd, Cell.Empty),
        GameBox(CellPosition.BottomStart, Cell.Empty),
        GameBox(CellPosition.BottomCenter, Cell.Empty),
        GameBox(CellPosition.BottomEnd, Cell.O),
    )
    TicTacToeTheme {
        Surface {
            GameBoard(
                boxes = data,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .wrapContentHeight()
            )
        }
    }
}