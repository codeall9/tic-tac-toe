package io.codeall9.tictactoe.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.codeall9.tictactoe.R
import io.codeall9.tictactoe.model.AppState
import io.codeall9.tictactoe.model.rememberAppState
import io.codeall9.tictactoe.theme.TicTacToeTheme
import kotlinx.coroutines.launch

enum class TopLevelDestination(@StringRes val title: Int) {
    SINGLE_GAME(R.string.app_name),
    REPLAY(R.string.replay_title),
    ;
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun TicTacToeScaffold(
    appState: AppState = rememberAppState(),
    playedListContent: @Composable ColumnScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) = with(appState) {
    TicTacToeTheme {
        ModalBottomSheetLayout(
            sheetContent = { playedListContent() },
            sheetState = bottomSheetState,
        ) {
            Scaffold(
                topBar = {
                    TicTacToeAppbar(
                        title = stringResource(id = getTopLevelDestination().title),
                        navPlayedList = {
                            topLevelScope.launch { bottomSheetState.show() }
                        }
                    )
                },
                content = content
            )
        }
    }
}

@Composable
private fun TicTacToeAppbar(
    menuVisible: MutableState<Boolean> = remember { mutableStateOf(false) },
    title: String,
    navPlayedList: () -> Unit,
) {
    val (isExpanded, showMenu) = menuVisible
    val onReplayMenuClick = {
        showMenu(false)
        navPlayedList()
    }
    TopAppBar(
        title = { Text(text = title) },
        actions = {
            IconButton(
                onClick = { showMenu(!isExpanded) },
                content = {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = "more")
                }
            )
            DropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { showMenu(false) },
                modifier = Modifier.fillMaxWidth(0.5f)
            ) {
                DropdownMenuItem(onClick = onReplayMenuClick) {
                    Icon(imageVector = Icons.Default.History, contentDescription = "Replay")
                    Text(
                        text = "Played List",
                        maxLines = 1,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun ScaffoldPreview() {
    TicTacToeScaffold(
        playedListContent = {
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            )
        },
        content = {
            Box(modifier = Modifier.fillMaxSize())
        },
    )
}