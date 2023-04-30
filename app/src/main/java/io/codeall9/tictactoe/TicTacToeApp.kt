package io.codeall9.tictactoe

import android.app.Application
import io.codeall9.tictactoe.di.coroutineModule
import io.codeall9.tictactoe.di.gameModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TicTacToeApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            if (BuildConfig.DEBUG) androidLogger()

            androidContext(applicationContext)
            modules(coroutineModule, gameModule)
        }
    }
}