package io.codeall9.history.room

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.Instant

@ProvidedTypeConverter
internal class InstantConverter(
    private val toInstant: Long.() -> Instant = { Instant.ofEpochMilli(this) },
    private val toTimeMilli: Instant.() -> Long = { toEpochMilli() },
) {

    @TypeConverter
    fun fromEpochMilli(epochMilli: Long?): Instant? {
        return epochMilli?.toInstant()
    }

    @TypeConverter
    fun toEpochMilli(instant: Instant?): Long? {
        return instant?.toTimeMilli()
    }
}