package io.codeall9.tictactoe.replay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import io.codeall9.core.history.event.GameResult
import io.codeall9.core.history.repository.GetRecentGameResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RecentGameViewModel(
    private val getPlayedList: GetRecentGameResult,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val ioContext get() = viewModelScope.coroutineContext + ioDispatcher

    private val total = MutableLiveData(10u)
    val recentList: LiveData<List<GameResult>> = total
        .switchMap { value ->
            getPlayedList(value).asLiveData(ioContext)
        }

    fun onTotalUpdated(value: UInt) {
        total.value = value
    }
}