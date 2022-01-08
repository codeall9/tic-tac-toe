package io.codeall9.tictactoe.replay

import androidx.lifecycle.*
import io.codeall9.history.GetRecentPlayedGames
import io.codeall9.history.PlayedGame
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class RecentGameViewModel(
    private val getPlayedList: GetRecentPlayedGames,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val ioContext get() = viewModelScope.coroutineContext + ioDispatcher

    private val total = MutableLiveData(10u)
    val recentList: LiveData<List<PlayedGame>> = total
        .switchMap { value ->
            getPlayedList(value).asLiveData(ioContext)
        }

    fun onTotalUpdated(value: UInt) {
        total.value = value
    }
}