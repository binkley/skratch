package hm.binkley.labs.skratch.collections

import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import hm.binkley.labs.skratch.collections.Value.Nonce
import hm.binkley.labs.skratch.collections.Value.RuleValue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ValueSetMockTest {
    private val layer = 0
    private val set = ValueSet(layer)
    private val key = "foo"

    @Test
    fun shouldSizeWhenEmpty() {
        assertEquals(0, set.size)
    }

    @Test
    fun shouldStartEmpty() {
        assertTrue(set.isEmpty())
    }

    @Test
    fun shouldGetLayer() {
        assertEquals(layer, set.layer)
    }

    @Test
    fun shouldReplaceWhenAddingSameKey() {
        val entry = spy(ValueEntry(key, RuleValue { _, _ -> 3 }))
        val set = ValueSet(layer, mutableSetOf(entry))

        val other = spy(ValueEntry(key, spy(RuleValue { _, _ -> 4 })))
        set.add(other)

        assertEquals(1, set.size)

        verify(entry, times(1)).replaceWith(layer, other)
        verify(other, times(1)).add(layer)
    }

    @Test
    fun shouldRemoveWhenAddingNothing() {
        val entry = spy(ValueEntry(key, RuleValue { _, _ -> 3 }))
        val set = ValueSet(layer, mutableSetOf(entry))

        set.add(ValueEntry(key, Nonce))

        assertTrue(set.isEmpty())

        verify(entry, times(1)).remove(layer)
    }

    @Test
    fun shouldRemoveWhenPresent() {
        val entry = spy(ValueEntry(key, RuleValue { _, _ -> 3 }))
        val set = ValueSet(layer, mutableSetOf(entry))

        set.remove(entry)

        assertTrue(set.isEmpty())

        verify(entry, times(1)).remove(layer)
    }

    @Test
    fun shouldNotRemoveWhenAbsent() {
        val entry = spy(ValueEntry(key, RuleValue { _, _ -> 3 }))

        set.remove(entry)

        verify(entry, never()).remove(layer)
    }
}
