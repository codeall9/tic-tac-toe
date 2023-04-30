package io.codeall9.tictactoe.di

import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

enum class KoinDispatcher {
    IO, COMPUTATION, UI
}

val coroutineModule = module {
    single(named(KoinDispatcher.COMPUTATION)) { Dispatchers.Default }
    single(named(KoinDispatcher.IO)) { Dispatchers.IO }
    single(named(KoinDispatcher.UI)) { Dispatchers.Main }
}