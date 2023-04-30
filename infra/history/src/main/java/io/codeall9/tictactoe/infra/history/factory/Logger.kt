package io.codeall9.tictactoe.infra.history.factory

import android.util.Log
import io.codeall9.tictactoe.infra.history.comman.HandleException


internal val logAndroidError: HandleException = HandleException { throwable ->
    Log.e("HistoryModule", "", throwable)
}