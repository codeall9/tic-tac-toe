package io.codeall9.history.services

import android.util.Log

public typealias LogNonFatalException = (Throwable) -> Unit

internal val consoleLogger: LogNonFatalException by lazy { provideConsoleLogger() }

public fun provideDebugLogger(tag: String = "HistoryModule"): LogNonFatalException = { throwable ->
    Log.e(tag, "Non-fatal event happen", throwable)
}

internal fun provideConsoleLogger(): LogNonFatalException = { throwable ->
    println(throwable)
}
