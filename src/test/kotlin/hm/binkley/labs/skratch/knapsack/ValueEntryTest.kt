package hm.binkley.labs.skratch.knapsack

import hm.binkley.labs.skratch.knapsack.Value.Nonce
import hm.binkley.labs.skratch.knapsack.Value.RuleValue
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ValueEntryTest {
    private val key = "foo"
    private val rule: Rule<Int> = { _, _ -> 3 }
    private val value = spyk(RuleValue(rule))
    private val entry = ValueEntry(key, value)

    @Test
    fun shouldGetKey() {
        assertEquals(key, entry.key)
    }

    @Test
    fun shouldGetValue() {
        assertSame(value, entry.value)
    }

    @Test
    fun shouldComplainOnSetValue() {
        assertThrows(UnsupportedOperationException::class.java) {
            entry.setValue(Nonce)
        }
    }

    @Test
    fun shouldRemove() {
        val layer = 1
        every { value.remove(layer, key) } just Runs

        entry.remove(layer)

        verify { value.remove(layer, key) }
    }

    @Test
    fun shouldReplaceWith() {
        val layer = 1
        val other = RuleValue { _, _ -> 4 }
        every { value.replaceWith(layer, key, other) } just Runs

        entry.replaceWith(layer, ValueEntry(key, other))

        verify { value.replaceWith(layer, key, other) }
    }

    @Test
    fun shouldAdd() {
        val layer = 1
        every { value.add(layer, key) } just Runs

        entry.add(layer)

        verify { value.add(layer, key) }
    }
}
