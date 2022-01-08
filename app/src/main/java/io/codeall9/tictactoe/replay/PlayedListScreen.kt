package io.codeall9.tictactoe.replay

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.codeall9.engine.model.Player
import io.codeall9.history.GameTied
import io.codeall9.history.PlayedGame
import io.codeall9.history.PlayerWon
import io.codeall9.history.toGameId
import io.codeall9.tictactoe.theme.TicTacToeTheme
import java.util.*

@Composable
fun PlayedListScreen(
    modifier: Modifier = Modifier,
    viewModel: RecentGameViewModel = viewModel(RecentGameViewModel::class.java),
    navDetail: (PlayedGame) -> Unit = { }
) {
    val recent by viewModel.recentList.observeAsState(initial = emptyList())
    PlayedListScreen(
        modifier = modifier,
        games = recent,
        navigateToDetail = navDetail
    )
}

@Composable
fun PlayedListScreen(
    modifier: Modifier = Modifier,
    games: List<PlayedGame>,
    navigateToDetail: (PlayedGame) -> Unit = { }
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(key1 = games, block = {
        lazyListState.scrollToItem(0)
    })
    LazyColumn(modifier = modifier, state = lazyListState) {
       items(games, { it.hashCode() }) {
           GameResultCard(playedGame = it, navigateToDetail = navigateToDetail)
       }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private inline fun GameResultCard(
    playedGame: PlayedGame,
    crossinline navigateToDetail: (PlayedGame) -> Unit
) {
    Card(
        onClick = { navigateToDetail(playedGame) },
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        when (playedGame) {
            is PlayerWon -> WinnerContent(playedGame)
            is GameTied -> TiedContent()
        }
    }
}

@Composable
private fun WinnerContent(result: PlayerWon) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .background(Color.LightGray)
    ) {
        Surface(
            content = {
                when(result.winner) {
                    Player.O -> Icon(imageVector = Icons.Outlined.Circle, contentDescription = "O")
                    Player.X -> Icon(imageVector = Icons.Default.Close, contentDescription = "X")
                }
            },
            color = MaterialTheme.colors.primary,
            modifier = Modifier.size(96.dp)
        )
        Text(
            text = "Win",
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Composable
private fun TiedContent() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .background(MaterialTheme.colors.secondaryVariant)
    ) {
        Text(
            text = "Tied",
            style = MaterialTheme.typography.h4,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview(name = "Played List Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun PlayedListLightPreview() {
    TicTacToeTheme {
        PlayedListScreen(
            Modifier.fillMaxSize(),
            listOf(
                GameTied(UUID.randomUUID().toGameId()),
                PlayerWon(UUID.randomUUID().toGameId(), Player.O),
                PlayerWon(UUID.randomUUID().toGameId(), Player.X),
                PlayerWon(UUID.randomUUID().toGameId(), Player.X),
                GameTied(UUID.randomUUID().toGameId()),
                PlayerWon(UUID.randomUUID().toGameId(), Player.O),
            ),
        )
    }
}

@Preview(name = "Played List Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PlayedListPreview() {
    TicTacToeTheme {
        PlayedListScreen(
            Modifier.fillMaxSize(),
            listOf(
                PlayerWon(UUID.randomUUID().toGameId(), Player.X),
                GameTied(UUID.randomUUID().toGameId()),
                PlayerWon(UUID.randomUUID().toGameId(), Player.O),
            ),
        )
    }
}