package io.codeall9.tictactoe

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.codeall9.tictactoe.components.TicTacToeScaffold
import io.codeall9.tictactoe.game.GameViewModel
import io.codeall9.tictactoe.game.TicTacToeScreen

class MainActivity : AppCompatActivity() {

    private val gameViewModel by viewModels<GameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeScaffold {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    TicTacToeScreen(
                        board = gameViewModel.boardState,
                        onPlayerMove = gameViewModel::onPlayerMove,
                        onRestart = gameViewModel::resetGame,
                        modifier = Modifier.padding(4.dp)
                            .fillMaxWidth()
                            .wrapContentHeight()
                    )
                    GameResult(gameViewModel)
                }
            }
        }
    }

    @Composable
    private fun GameResult(viewModel: GameViewModel) {
        if (!viewModel.isGameOverState) return

        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = stringResource(id = R.string.game_over)) },
            text = {
                viewModel.winnerState
                    ?.let { Text(text = stringResource(id = R.string.game_winner, it.name)) }
                    ?: Text(text = stringResource(id = R.string.game_tie))
            },
            confirmButton = {
                Button(onClick = viewModel::resetGame) {
                    Text(text = stringResource(id = android.R.string.ok))
                }
            }
        )
    }
}