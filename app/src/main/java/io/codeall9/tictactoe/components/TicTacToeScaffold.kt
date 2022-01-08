package io.codeall9.tictactoe.components

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
import io.codeall9.tictactoe.theme.TicTacToeTheme
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun TicTacToeScaffold(
    playedListContent: @Composable ColumnScope.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val navPlayedList: () -> Unit = {
        scope.launch { state.show() }
    }
    TicTacToeTheme {
        ModalBottomSheetLayout(
            sheetContent = { playedListContent() },
            sheetState = state,
        ) {
            Scaffold(
                topBar = { TicTacToeAppbar(navPlayedList = navPlayedList) },
                content = content
            )
        }
    }
}

@Composable
private fun TicTacToeAppbar(
    menuVisible: MutableState<Boolean> = remember { mutableStateOf(false) },
    navPlayedList: () -> Unit,
) {
    val (isExpanded, showMenu) = menuVisible
    val onReplayMenuClick = {
        showMenu(false)
        navPlayedList()
    }
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
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