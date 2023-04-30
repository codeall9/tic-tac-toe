package io.codeall9.core.history.model

import java.util.UUID

@JvmInline
public value class HistoryId internal constructor(public val value: String) {

    public companion object {

        @JvmStatic
        public fun of(uuid: UUID): HistoryId {
            return HistoryId(uuid.toString())
        }
    }
}