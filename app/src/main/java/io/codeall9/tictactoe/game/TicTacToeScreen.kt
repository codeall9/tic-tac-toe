package io.codeall9.tictactoe.game

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.codeall9.engine.model.Cell
import io.codeall9.engine.model.CellPosition
import io.codeall9.tictactoe.R
import io.codeall9.tictactoe.components.GameBoard
import io.codeall9.tictactoe.theme.TicTacToeTheme

@Composable
fun TicTacToeScreen(
    modifier: Modifier = Modifier,
    boxes: List<Box>,
    onPlayerMove: (CellPosition) -> Unit = { /* no-op */ },
    onRestart: () -> Unit = { /* no-op */ },
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier,
    ) {
        GameBoard(
            boxes = boxes,
            onCellClick = onPlayerMove
        )
        Button(
            onClick = onRestart,
            modifier = Modifier
                .padding(16.dp)
                .heightIn(min = 56.dp)
                .fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Restart")
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = stringResource(id = R.string.button_restart))
        }
    }
}

@Preview("TicTacToe Light")
@Composable
private fun TicTacToeLightPreview() {
    TicTacToeTheme(false) {
        Surface {
            TicTacToeScreen(
                boxes = listOf(
                    Box(CellPosition.TopStart, Cell.X), Box(CellPosition.TopCenter, Cell.Empty), Box(
                        CellPosition.TopEnd, Cell.X),
                    Box(CellPosition.CenterStart, Cell.Empty), Box(CellPosition.Center, Cell.O), Box(
                        CellPosition.CenterEnd, Cell.Empty),
                    Box(CellPosition.BottomStart, Cell.O), Box(CellPosition.BottomCenter, Cell.Empty), Box(
                        CellPosition.BottomEnd, Cell.Empty),
                ),
            )
        }
    }
}

@Preview("TicTacToe Dark")
@Composable
private fun TicTacToeDarkPreview() {
    TicTacToeTheme(true) {
        Surface {
            TicTacToeScreen(
                boxes = listOf(
                    Box(CellPosition.TopStart, Cell.Empty), Box(CellPosition.TopCenter, Cell.O), Box(
                        CellPosition.TopEnd, Cell.X),
                    Box(CellPosition.CenterStart, Cell.O), Box(CellPosition.Center, Cell.O), Box(
                        CellPosition.CenterEnd, Cell.X),
                    Box(CellPosition.BottomStart, Cell.Empty), Box(CellPosition.BottomCenter, Cell.X), Box(
                        CellPosition.BottomEnd, Cell.O),
                ),
            )
        }
    }
}