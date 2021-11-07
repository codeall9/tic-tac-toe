package io.codeall9.history

import java.util.*

@JvmInline
public value class GameId internal constructor(internal val value: String)

public fun UUID.toGameId(): GameId {
    return GameId(toString())
}