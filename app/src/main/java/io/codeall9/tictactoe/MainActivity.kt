package io.codeall9.tictactoe

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.codeall9.tictactoe.components.TicTacToeScaffold
import io.codeall9.tictactoe.game.GameViewModel
import io.codeall9.tictactoe.game.TicTacToeScreen
import io.codeall9.tictactoe.replay.HistoryViewModel
import io.codeall9.tictactoe.replay.PlayedListScreen
import io.codeall9.tictactoe.replay.RecentGameViewModel
import io.codeall9.tictactoe.replay.ReplayDetailScreen
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.UUID

class MainActivity : AppCompatActivity() {

    private val gameViewModel by viewModel<GameViewModel>()

    private val recentGameViewModel by viewModel<RecentGameViewModel>()

    private val historyViewModel by viewModel<HistoryViewModel>()

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val state: ModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
            val scope = rememberCoroutineScope()
            TicTacToeScaffold(
                bottomSheetState = state,
                playedListContent = {
                    PlayedListScreen(
                        viewModel = recentGameViewModel,
                        navDetail = { result ->
                            scope.launch { state.hide() }
                            // TODO: change Appbar title
                            navController.navigate("replay/${result.historyId.value}")
                        },
                        modifier = Modifier
                            .heightIn(min = 128.dp)
                            .fillMaxWidth()
                    )
                },
                content = { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "game",
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                            .wrapContentHeight()
                    ) {
                        composable("game") {
                            TicTacToeScreen(viewModel = gameViewModel, modifier = Modifier.fillMaxSize())
                        }
                        composable(
                            route = "replay/{historyId}",
                            arguments = listOf(navArgument("historyId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val uuid by remember {
                                derivedStateOf {
                                    backStackEntry.arguments
                                        ?.runCatching { UUID.fromString(getString("historyId")) }
                                        ?.getOrNull()
                                }
                            }
                            val historyId = uuid ?: run {
                                Toast.makeText(LocalContext.current, "missing history id", Toast.LENGTH_SHORT).show()
                                navController.navigate("game")
                                return@composable
                            }

                            ReplayDetailScreen(id = historyId, viewModel = historyViewModel, modifier = Modifier.fillMaxSize())
                        }
                    }
                }
            )
        }
    }
}