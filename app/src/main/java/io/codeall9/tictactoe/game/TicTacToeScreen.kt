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
import io.codeall9.tictactoe.R
import io.codeall9.tictactoe.theme.TicTacToeTheme

@Composable
fun TicTacToeScreen(
    modifier: Modifier = Modifier,
    board: List<Char>,
    onPlayerMove: (Int) -> Unit = {},
    onRestart: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier,
    ) {
        GameBoard(
            cells = board,
            onItemClick = onPlayerMove
        )
        Button(
            onClick = onRestart,
            modifier = Modifier
                .padding(16.dp)
                .preferredHeightIn(min = 56.dp)
                .fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Filled.Refresh)
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
                board = listOf(
                    'X', ' ', 'X',
                    ' ', 'O', ' ',
                    'O', ' ', ' ',
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
                board = listOf(
                    ' ', 'O', 'X',
                    'O', 'O', 'X',
                    ' ', 'X', 'O',
                ),
            )
        }
    }
}