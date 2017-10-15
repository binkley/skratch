package hm.binkley.labs.skratch.collections

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import hm.binkley.labs.skratch.collections.Value.Nonce
import hm.binkley.labs.skratch.collections.Value.RuleValue
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ValueMapMockTest {
    private val database: Database = mock()
    private val layer = 0
    private val key = "foo"

    @Test
    fun shouldGetLayer() {
        assertEquals(layer, ValueMap(database, layer, ValueSet(layer)).layer)
    }

    @Test
    fun shouldForwardWhenSettingNonce() {
        val set = spy(ValueSet(layer))
        doReturn(true).whenever(set).add(any())
        val map = ValueMap(database, layer, set)

        map[key] = null

        argumentCaptor<ValueEntry>().apply {
            verify(set, times(1)).add(capture())
            assertEquals(key, firstValue.key)
            assertSame(Nonce, firstValue.value)
        }
    }

    @Test
    fun shouldForwardWhenSettingValue() {
        val value = "3"
        val set = spy(ValueSet(layer))
        doReturn(true).whenever(set).add(any())
        val map = ValueMap(database, layer, set)

        map[key] = value

        argumentCaptor<ValueEntry>().apply {
            verify(set, times(1)).add(capture())
            assertEquals(key, firstValue.key)
            assertEquals(database.value(value), firstValue.value)
        }
    }

    @Test
    fun shouldForwardWhenSettingRule() {
        val rule: Rule<Int> = { _, _ -> 3 }
        val set = spy(ValueSet(layer))
        doReturn(true).whenever(set).add(any())
        val map = ValueMap(database, layer, set)

        map[key] = rule

        argumentCaptor<ValueEntry>().apply {
            verify(set, times(1)).add(capture())
            assertEquals(key, firstValue.key)
            assertEquals(RuleValue(rule), firstValue.value)
        }
    }

    @Test
    fun shouldForwardWhenRemoving() {
        val entry = ValueEntry(key, RuleValue { _, _ -> 3 })
        val dummy = mutableSetOf(entry)
        val set = spy(ValueSet(layer, mutableSetOf(entry)))
        doReturn(dummy.iterator()).whenever(set).iterator()
        val map = ValueMap(database, layer, set)

        map.remove(key)

        assertTrue(dummy.isEmpty())
    }
}
