package io.codeall9.tictactoe

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.codeall9.engine.initLocalGame
import io.codeall9.history.recordingTicTacToe
import io.codeall9.history.services.provideRecentPlayedGame
import io.codeall9.tictactoe.components.TicTacToeScaffold
import io.codeall9.tictactoe.game.*
import io.codeall9.tictactoe.replay.PlayedListScreen
import io.codeall9.tictactoe.replay.RecentGameViewModel

class MainActivity : AppCompatActivity() {

    private val gameViewModel by viewModels<GameViewModel>() {
        GameViewModelFactory(recordingTicTacToe(applicationContext, initLocalGame))
    }

    private val recentGameViewModel by viewModels<RecentGameViewModel>() {
        object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return RecentGameViewModel(provideRecentPlayedGame(applicationContext)) as T
            }

        }
    }

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