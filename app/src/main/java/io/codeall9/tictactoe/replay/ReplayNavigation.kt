package io.codeall9.tictactoe.replay

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import io.codeall9.core.history.model.HistoryId
import java.util.UUID

const val REPLAY_ARG_HISTORY_ID = "historyId"
const val REPLAY_ROUTE = "replay"

fun NavController.navigateReplayDetail(historyId: HistoryId) {
    val encodedId = Uri.encode(historyId.value)
    navigate("$REPLAY_ROUTE/$encodedId") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.replayDetailGraph(
    historyViewModel: HistoryViewModel,
    onNavigateUp: () -> Unit = {},
) {
    composable(
        route = "$REPLAY_ROUTE/{$REPLAY_ARG_HISTORY_ID}",
        arguments = listOf(
            navArgument(REPLAY_ARG_HISTORY_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val uuid by rememberArgHistoryId(backStackEntry)

        val historyId = uuid ?: run {
            Toast.makeText(LocalContext.current, "missing history id", Toast.LENGTH_SHORT).show()
            onNavigateUp()
            return@composable
        }
        ReplayDetailScreen(id = historyId, viewModel = historyViewModel, modifier = Modifier.fillMaxSize())
    }
}

@Composable
private fun rememberArgHistoryId(backStackEntry: NavBackStackEntry) = remember {
    derivedStateOf {
        backStackEntry.arguments
            ?.runCatching { UUID.fromString(getString(REPLAY_ARG_HISTORY_ID)) }
            ?.getOrNull()
    }
}