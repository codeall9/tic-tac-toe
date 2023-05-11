package io.codeall9.tictactoe.infra.history.transform

import io.codeall9.history.test.NUM_TESTS
import io.codeall9.history.test.randomEpochMilli
import io.codeall9.history.test.randomInstant
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class InstantConverterTest {

    private val converter = InstantConverter()

    @Test
    @DisplayName("For any null epochMilli, calling fromEpochMilli should return null.")
    fun fromEpochMilliWithNull() {
        assertEquals(null, converter.fromEpochMilli(null))
    }

    @Test
    @DisplayName("For any null Instant, calling toEpochMilli should return null.")
    fun toEpochMilliWithNull() {
        assertEquals(null, converter.toEpochMilli(null))
    }

    @RepeatedTest(NUM_TESTS)
    @DisplayName("Converting a Long epochMilli value to Instant and back to epochMilli should return the same value for any input.")
    fun convertEpochMilliToInstantAndBack() {
        // Given
        val expected = randomEpochMilli()
        // When
        val actual = converter
            .fromEpochMilli(expected)
            ?.let { converter.toEpochMilli(it) }
        // Then
        assertEquals(expected, actual)
    }

    @RepeatedTest(NUM_TESTS)
    @DisplayName("Converting an Instant to epochMilli and back to Instant should return the same value for any input.")
    fun convertInstantToEpochMilliAndBack() {
        // Given
        val expected = randomInstant()
        // When
        val actual = converter
            .toEpochMilli(expected)
            ?.let { converter.fromEpochMilli(it) }
        // Then
        assertEquals(expected, actual)
    }
}