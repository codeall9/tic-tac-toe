package io.codeall9.engine

import java.util.concurrent.atomic.AtomicInteger

/**
 * Executing a [block] exactly [total] times
 *
 * @param total maximum invoke times
 * @param block function block
 *
 */
inline fun <R> requireInvokeTimes(total: UInt, crossinline block: () -> R) : () -> R {
    val invoked = AtomicInteger(0)
    return requireInvoke(
        block = block,
        lazyMessage = { "function invoke more than $total times" },
        predicate = { invoked.incrementAndGet() <= total.toInt() }
    )
}

/**
 * Executing a [block] matching the given [predicate]
 *
 */
inline fun <R> requireInvoke(
    crossinline block: () -> R,
    crossinline lazyMessage: () -> Any = { "Failed executing function by requirement" },
    crossinline predicate: () -> Boolean,
) : () -> R {
    return {
        check(predicate(), lazyMessage)
        block()
    }
}

/**
 * Executing a suspend [block] matching the given [predicate]
 *
 */
inline fun <R> requireInvoke(
    crossinline block: suspend () -> R,
    crossinline lazyMessage: () -> Any = { "Failed executing function by requirement" },
    crossinline predicate: suspend () -> Boolean,
) : suspend () -> R {
    return {
        check(predicate(), lazyMessage)
        block()
    }
}