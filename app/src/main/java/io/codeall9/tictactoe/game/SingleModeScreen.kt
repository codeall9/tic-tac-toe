package io.codeall9.tictactoe.game

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.codeall9.tictactoe.R
import io.codeall9.tictactoe.components.GameBoard
import io.codeall9.tictactoe.core.engine.model.Cell
import io.codeall9.tictactoe.core.engine.model.CellPosition
import io.codeall9.tictactoe.model.GameBox
import io.codeall9.tictactoe.theme.TicTacToeTheme

@Composable
fun SingleModeScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = viewModel(GameViewModel::class.java),
    onAction: (PlayerAction) -> Unit = viewModel::onActionDispatched,
) {
    val gridBoxes by viewModel.gameBoxes.observeAsState(emptyList())
    val status by viewModel.gameStatus.observeAsState(initial = GameStatus.Ongoing)

    SingleModeScreen(
        box = gridBoxes,
        status = status,
        onPlayerMove = { position -> onAction(MarkPosition(position)) },
        onRestart = { onAction(LaunchNewGame) },
        modifier = modifier
    )
}

@Composable
fun SingleModeScreen(
    modifier: Modifier = Modifier,
    box: List<GameBox>,
    status: GameStatus,
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
    when (status) {
        is GameStatus.IsDraw -> {
            AlertGameTie(onConfirm = { onRestart() })
        }
        is GameStatus.PlayerWin -> {
            AlertWinner(winnerName = status.winner.name, onConfirm = { onRestart() })
        }
        else -> {/* no-op */}
    }
}

@Composable
private fun AlertWinner(
    modifier: Modifier = Modifier,
    winnerName: String,
    onDismissRequest: () -> Unit = { /* no-op */ },
    onConfirm: () -> Unit = { /* no-op */ },
) {
    AlertGameResult(
        text = { Text(text = stringResource(id = R.string.game_winner, winnerName)) },
        onConfirm = onConfirm,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    )
}

@Composable
private fun AlertGameTie(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit = { /* no-op */ },
    onConfirm: () -> Unit = { /* no-op */ },
) {
    AlertGameResult(
        text = { Text(text = stringResource(id = R.string.game_tie)) },
        onConfirm = onConfirm,
        onDismissRequest = onDismissRequest,
        modifier = modifier
    )
}

@Composable
private fun AlertGameResult(
    modifier: Modifier = Modifier,
    text: @Composable (() -> Unit)? = null,
    title: @Composable (() -> Unit)? = { Text(text = stringResource(id = R.string.game_over)) },
    onConfirm: () -> Unit = { /* no-op */ },
    onDismissRequest: () -> Unit = { /* no-op */ },
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = title,
        text = text,
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = stringResource(id = android.R.string.ok))
            }
        },
        modifier = modifier
    )
}

@Preview("TicTacToe Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun TicTacToeLightPreview() {
    TicTacToeTheme(false) {
        Surface {
            SingleModeScreen(
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
                status = GameStatus.Ongoing,
            )
        }
    }
}

@Preview("TicTacToe Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TicTacToeDarkPreview() {
    TicTacToeTheme {
        Surface {
            SingleModeScreen(
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
                status = GameStatus.Ongoing,
            )
        }
    }
}