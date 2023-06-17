package io.codeall9.tictactoe.replay

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import io.codeall9.core.history.model.HistoryId
import io.codeall9.core.history.repository.GetGameHistory
import io.codeall9.tictactoe.core.engine.model.Board
import io.codeall9.tictactoe.model.GameBox
import io.codeall9.tictactoe.model.toBoxList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import java.util.UUID
import kotlin.math.roundToInt

class HistoryViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getHistory: GetGameHistory,
    private val worker: CoroutineDispatcher = Dispatchers.Default,
): ViewModel() {

    private val selectedRound = MutableStateFlow(1)

    private val history = savedStateHandle
        .getLiveData<UUID>(REPLAY_ARG_HISTORY_ID)
        .asFlow()
        .distinctUntilChanged()
        .mapNotNull { uuid -> getHistory(HistoryId.of(uuid)) }
        .shareIn(viewModelScope, SharingStarted.Lazily, 1)

    val gameBoxes: LiveData<List<GameBox>> = history
        .combine(selectedRound) { history, round ->
            history[round.coerceIn(1..history.totalRounds)] ?: Board.Empty
        }
        .map { it.toBoxList() }
        .flowOn(worker)
        .asLiveData()

    val gameRounds: LiveData<ClosedFloatingPointRange<Float>> = history
        .map { 1f..it.totalRounds.toFloat() }
        .asLiveData()


    fun replayGame(uuid: UUID) {
        savedStateHandle[REPLAY_ARG_HISTORY_ID] = uuid
        selectedRound.update { 1 }
    }

    fun onRoundChanged(value: Float) {
        selectedRound.update { value.roundToInt() }
    }
}