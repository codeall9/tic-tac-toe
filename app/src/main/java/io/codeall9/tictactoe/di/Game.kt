package io.codeall9.tictactoe.di

import io.codeall9.core.history.usecase.LaunchTicTacToe
import io.codeall9.tictactoe.core.engine.initLocalGame
import io.codeall9.tictactoe.game.GameViewModel
import io.codeall9.tictactoe.infra.history.factory.provideGetGameHistory
import io.codeall9.tictactoe.infra.history.factory.provideGetRecentGameResult
import io.codeall9.tictactoe.infra.history.factory.provideLogGameTransition
import io.codeall9.tictactoe.replay.HistoryViewModel
import io.codeall9.tictactoe.replay.RecentGameViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val gameModule = module {
    factory { provideLogGameTransition(appContext = get()) }
    factory { provideGetRecentGameResult(appContext = get()) }
    factory { provideGetGameHistory(appContext = get()) }
    factory {
        LaunchTicTacToe(logger = get(), launchGame = initLocalGame)
    }

    viewModel { GameViewModel(initGame = get(), worker = get(named(KoinDispatcher.COMPUTATION))) }
    viewModel {
        RecentGameViewModel(
            getPlayedList = get(),
            ioDispatcher = get(named(KoinDispatcher.IO))
        )
    }
    viewModel {
        HistoryViewModel(
            savedStateHandle = get(),
            getHistory = get(),
            worker = get(named(KoinDispatcher.COMPUTATION))
        )
    }
}