package io.codeall9.tictactoe

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.codeall9.tictactoe.components.TicTacToeScaffold
import io.codeall9.tictactoe.game.GameViewModel
import io.codeall9.tictactoe.game.LaunchNewGame
import io.codeall9.tictactoe.game.MarkPosition
import io.codeall9.tictactoe.game.TicTacToeScreen

class MainActivity : AppCompatActivity() {

    private val gameViewModel by viewModels<GameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dispatch = gameViewModel::onNewAction
        setContent {
            TicTacToeScaffold {
                val gridBoxes by gameViewModel.gameBoxes.observeAsState(emptyList())
                val gameTie by gameViewModel.gameTie.observeAsState(initial = false)
                val gameWinner by gameViewModel.gameWinner.observeAsState()

                TicTacToeScreen(
                    box = gridBoxes,
                    onPlayerMove = { position -> dispatch(MarkPosition(position)) },
                    onRestart = { dispatch(LaunchNewGame) },
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
                val winner = gameWinner?.name
                if (winner != null) {
                    AlertWinner(winnerName = winner, onConfirm = { dispatch(LaunchNewGame) })
                } else if (gameTie) {
                    AlertGameTie(onConfirm = { dispatch(LaunchNewGame) })
                }
            }
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
}