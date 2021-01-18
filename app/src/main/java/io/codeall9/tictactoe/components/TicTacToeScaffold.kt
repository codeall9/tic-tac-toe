package io.codeall9.tictactoe.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.codeall9.tictactoe.R
import io.codeall9.tictactoe.theme.TicTacToeTheme

@Composable
fun TicTacToeScaffold(content: @Composable() (PaddingValues) -> Unit) {
    TicTacToeTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.app_name)) }
                )
            },
            bodyContent = content
        )
    }
}