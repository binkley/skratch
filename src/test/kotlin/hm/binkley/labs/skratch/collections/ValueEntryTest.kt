package hm.binkley.labs.skratch.collections

import com.nhaarman.mockito_kotlin.doNothing
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import hm.binkley.labs.skratch.collections.Value.Nonce
import hm.binkley.labs.skratch.collections.Value.RuleValue
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ValueEntryTest {
    private val key = "foo"
    private val rule: Rule<Int> = { _, _ -> 3 }
    private val value = spy(RuleValue(rule))
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
        doNothing().whenever(value).remove(layer, key)

        entry.remove(layer)

        verify(value).remove(layer, key)
    }

    @Test
    fun shouldReplaceWith() {
        val layer = 1
        val other = RuleValue { _, _ -> 4 }
        doNothing().whenever(value).replaceWith(layer, key, other)

        entry.replaceWith(layer, ValueEntry(key, other))

        verify(value).replaceWith(layer, key, other)
    }

    @Test
    fun shouldAdd() {
        val layer = 1
        doNothing().whenever(value).add(layer, key)

        entry.add(layer)

        verify(value).add(layer, key)
    }
}
