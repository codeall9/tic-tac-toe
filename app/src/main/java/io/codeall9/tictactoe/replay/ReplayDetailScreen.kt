package io.codeall9.tictactoe.replay

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.codeall9.tictactoe.components.GameBoard
import io.codeall9.tictactoe.core.engine.model.Cell
import io.codeall9.tictactoe.core.engine.model.CellPosition
import io.codeall9.tictactoe.model.GameBox
import io.codeall9.tictactoe.theme.TicTacToeTheme
import java.util.UUID

@Composable
fun ReplayDetailScreen(
    modifier: Modifier = Modifier,
    id: UUID,
    viewModel: HistoryViewModel,
) = with (viewModel) {
    val boxes by gameBoxes.observeAsState(initial = emptyList())
    val range by gameRounds.observeAsState(initial = 1f..1f)

    LaunchedEffect(id) {
        viewModel.replayGame(id)
    }

    ReplayDetailScreen(
        modifier,
        boxes,
        range,
        ::onRoundChanged
    )
}

@Composable
fun ReplayDetailScreen(
    modifier: Modifier = Modifier,
    boxes: List<GameBox>,
    sliderRange: ClosedFloatingPointRange<Float> = 1f..9f,
    onRoundChanged: (Float) -> Unit = { },
) {
    var rounds by rememberSaveable { mutableStateOf(1f) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier,
    ) {
        GameBoard(boxes = boxes)
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIos,
                contentDescription = "ArrowBack",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .size(36.dp)
                    .weight(1f)
                    .clickable {
                        rounds = (rounds - 1f).coerceIn(sliderRange)
                        onRoundChanged(rounds)
                    },
            )
            Slider(
                value = rounds,
                valueRange = sliderRange,
                steps = 1,
                onValueChange = {
                    rounds = it
                    onRoundChanged(it)
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(2f),
            )
            Icon(
                imageVector = Icons.Filled.ArrowForwardIos,
                contentDescription = "ArrowForward",
                tint = MaterialTheme.colors.primary,
                modifier = Modifier
                    .size(36.dp)
                    .weight(1f)
                    .clickable {
                        rounds = (rounds + 1f).coerceIn(sliderRange)
                        onRoundChanged(rounds)
                    },
            )
        }
    }
}

@Preview("Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview("Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    TicTacToeTheme {
        ReplayDetailScreen(
            boxes = listOf(
                GameBox(CellPosition.TopStart, Cell.X),
                GameBox(CellPosition.TopCenter, Cell.Empty),
                GameBox(CellPosition.TopEnd, Cell.X),
                GameBox(CellPosition.CenterStart, Cell.Empty),
                GameBox(CellPosition.Center, Cell.O),
                GameBox(CellPosition.CenterEnd, Cell.Empty),
                GameBox(CellPosition.BottomStart, Cell.O),
                GameBox(CellPosition.BottomCenter, Cell.Empty),
                GameBox(CellPosition.BottomEnd, Cell.Empty),
            ),
        )
    }

}