package io.codeall9.tictactoe.game

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.codeall9.tictactoe.theme.TicTacToeTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameBoard(
    modifier: Modifier = Modifier,
    cells: List<Char>,
    onItemClick: (Int) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        cells = GridCells.Fixed(3)
    ) {
        itemsIndexed(cells) { index, cell ->
            GameCell(
                Modifier.padding(8.dp).aspectRatio(1.0f),
                mark = cell,
                onClick = { onItemClick(index) }
            )
        }
    }
}

@Composable
private fun GameCell(
    modifier: Modifier = Modifier,
    mark: Char,
    onClick: () -> Unit = {}
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        enabled = mark.isWhitespace()
    ) {
        Text(
            style = MaterialTheme.typography.h4.copy(textAlign = TextAlign.Center),
            text = mark.toString(),
        )
    }
}

@Preview
@Composable
private fun GameBoardPreview() {
    val data = listOf(
        'X', 'O', 'X',
        ' ', 'O', ' ',
        ' ', ' ', 'O',
    )
    TicTacToeTheme {
        Surface {
            GameBoard(cells = data) {  }
        }
    }
}