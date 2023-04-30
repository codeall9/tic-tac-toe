package io.codeall9.tictactoe

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.codeall9.tictactoe.components.TicTacToeScaffold
import io.codeall9.tictactoe.game.GameViewModel
import io.codeall9.tictactoe.game.TicTacToeScreen
import io.codeall9.tictactoe.replay.PlayedListScreen
import io.codeall9.tictactoe.replay.RecentGameViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val gameViewModel by viewModel<GameViewModel>()

    private val recentGameViewModel by viewModel<RecentGameViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeScaffold(
                playedListContent = {
                    PlayedListScreen(
                        viewModel = recentGameViewModel,
                        navDetail = {
                            Toast.makeText(this@MainActivity, "detail page", Toast.LENGTH_SHORT).show()
                            // TODO: detail page
                        },
                        modifier = Modifier
                            .heightIn(min = 128.dp)
                            .fillMaxWidth()
                    )
                },
                content = {
                    TicTacToeScreen(
                        viewModel = gameViewModel,
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            .wrapContentHeight()
                    )
                }
            )
        }
    }
}