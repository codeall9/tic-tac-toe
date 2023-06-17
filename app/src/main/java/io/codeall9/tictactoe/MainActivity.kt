package io.codeall9.tictactoe

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import io.codeall9.tictactoe.components.TicTacToeScaffold
import io.codeall9.tictactoe.game.GameViewModel
import io.codeall9.tictactoe.game.SINGLE_MODE_ROUTE
import io.codeall9.tictactoe.game.singleModeGraph
import io.codeall9.tictactoe.model.rememberAppState
import io.codeall9.tictactoe.replay.HistoryViewModel
import io.codeall9.tictactoe.replay.PlayedListScreen
import io.codeall9.tictactoe.replay.RecentGameViewModel
import io.codeall9.tictactoe.replay.navigateReplayDetail
import io.codeall9.tictactoe.replay.replayDetailGraph
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val gameViewModel by viewModel<GameViewModel>()

    private val recentGameViewModel by viewModel<RecentGameViewModel>()

    private val historyViewModel by viewModel<HistoryViewModel>()

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = rememberAppState()
            TicTacToeScaffold(
                appState = state,
                playedListContent = {
                    PlayedListScreen(
                        viewModel = recentGameViewModel,
                        navDetail = { result ->
                            state.topLevelScope.launch { state.bottomSheetState.hide() }
                            state.navController.navigateReplayDetail(result.historyId)
                        },
                        modifier = Modifier
                            .heightIn(min = 128.dp)
                            .fillMaxWidth()
                    )
                },
                content = { paddingValues ->
                    NavHost(
                        navController = state.navController,
                        startDestination = SINGLE_MODE_ROUTE,
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                            .wrapContentHeight(),
                    ) {
                        singleModeGraph(gameViewModel)
                        replayDetailGraph(historyViewModel, state.navController::popBackStack)
                    }
                }
            )
        }
    }
}