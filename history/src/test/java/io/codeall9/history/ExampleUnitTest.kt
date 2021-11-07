package io.codeall9.history

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class ExampleUnitTest {

    @Test
    fun `addition is correct`() {
//        assertEquals(4, 2 + 2L)
        Assertions.assertEquals(5, 2 + 3)
    }
}