package io.codeall9.tictactoe.game

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val SINGLE_MODE_ROUTE = "singleMode"

fun NavGraphBuilder.singleModeGraph(gameViewModel: GameViewModel) {
    composable("singleMode") {
        SingleModeScreen(viewModel = gameViewModel, modifier = Modifier.fillMaxSize())
    }
}