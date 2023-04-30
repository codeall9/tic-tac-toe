package io.codeall9.tictactoe.infra.history.transform

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import io.codeall9.tictactoe.infra.history.comman.Transform
import java.time.Instant

@ProvidedTypeConverter
internal class InstantConverter(
    private val toInstant: Transform<Long, Instant> = { Instant.ofEpochMilli(this) },
    private val toTimeMilli: Transform<Instant, Long> = { toEpochMilli() },
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