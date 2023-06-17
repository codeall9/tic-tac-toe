package io.codeall9.tictactoe.model

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import io.codeall9.tictactoe.components.TopLevelDestination
import io.codeall9.tictactoe.game.SINGLE_MODE_ROUTE
import io.codeall9.tictactoe.replay.REPLAY_ROUTE
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterialApi::class)
@Stable
class AppState(
    val topLevelScope: CoroutineScope,
    val navController: NavHostController,
    val bottomSheetState: ModalBottomSheetState,
) {

    val backStackEntry: State<NavBackStackEntry?>
        @Composable get() = navController.currentBackStackEntryAsState()

    val navDestination: NavDestination?
        @Composable get() = backStackEntry.value?.destination

    @Composable
    fun getTopLevelDestination(): TopLevelDestination {
        val route = navDestination
            ?.route
            ?: return TopLevelDestination.SINGLE_GAME
        return when {
            route.contains(SINGLE_MODE_ROUTE) -> TopLevelDestination.SINGLE_GAME
            route.contains(REPLAY_ROUTE) -> TopLevelDestination.REPLAY
            else -> TopLevelDestination.REPLAY
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberAppState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
    bottomSheetState: ModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
): AppState = remember {
    AppState(coroutineScope, navController, bottomSheetState)
}