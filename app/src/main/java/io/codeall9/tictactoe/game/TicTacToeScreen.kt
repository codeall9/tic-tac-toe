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
import io.codeall9.tictactoe.model.GameBox
import io.codeall9.tictactoe.theme.TicTacToeTheme

@Composable
fun TicTacToeScreen(
    modifier: Modifier = Modifier,
    box: List<GameBox>,
    onPlayerMove: (CellPosition) -> Unit = { /* no-op */ },
    onRestart: () -> Unit = { /* no-op */ },
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier,
    ) {
        GameBoard(
            boxes = box,
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
                box = listOf(
                    GameBox(CellPosition.TopStart, Cell.X),
                    GameBox(CellPosition.TopCenter, Cell.Empty),
                    GameBox(CellPosition.TopEnd, Cell.X),
                    GameBox(CellPosition.CenterStart, Cell.Empty),
                    GameBox(CellPosition.Center, Cell.O),
                    GameBox(CellPosition.CenterEnd, Cell.Empty),
                    GameBox(CellPosition.BottomStart, Cell.O),
                    GameBox(CellPosition.BottomCenter, Cell.Empty),
                    GameBox(CellPosition.BottomEnd, Cell.Empty),
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
                box = listOf(
                    GameBox(CellPosition.TopStart, Cell.Empty),
                    GameBox(CellPosition.TopCenter, Cell.O),
                    GameBox(CellPosition.TopEnd, Cell.X),
                    GameBox(CellPosition.CenterStart, Cell.O),
                    GameBox(CellPosition.Center, Cell.O),
                    GameBox(CellPosition.CenterEnd, Cell.X),
                    GameBox(CellPosition.BottomStart, Cell.Empty),
                    GameBox(CellPosition.BottomCenter, Cell.X),
                    GameBox(CellPosition.BottomEnd, Cell.O),
                ),
            )
        }
    }
}