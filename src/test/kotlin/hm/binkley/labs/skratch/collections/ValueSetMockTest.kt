package hm.binkley.labs.skratch.collections

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ValueSetMockTest {
    private val layer = 0
    private val set = ValueSet(layer)

    @Test
    fun shouldSizeWhenEmpty() {
        assertEquals(0, set.size)
    }

    @Test
    fun shouldStartEmpty() {
        assertTrue(set.isEmpty())
    }
}
