package hm.binkley.labs.skratch.collections

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import hm.binkley.labs.skratch.collections.Value.DatabaseValue
import hm.binkley.labs.skratch.collections.Value.Nonce
import hm.binkley.labs.skratch.collections.Value.RuleValue
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ValueMapMockTest {
    private val database: Database = mock()
    private val layer = 0
    private val key = "foo"

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
        val set = spy(ValueSet(layer))
        doReturn(true).whenever(set).add(any())
        val map = ValueMap(database, layer, set)
        val value = "3"

        map[key] = value

        argumentCaptor<ValueEntry>().apply {
            verify(set, times(1)).add(capture())
            assertEquals(key, firstValue.key)
            assertEquals(DatabaseValue(database, value), firstValue.value)
        }
    }

    @Test
    fun shouldForwardWhenSettingRule() {
        val set = spy(ValueSet(layer))
        doReturn(true).whenever(set).add(any())
        val map = ValueMap(database, layer, set)
        val rule: Rule<Int> = { _, _ -> 3 }

        map[key] = rule

        argumentCaptor<ValueEntry>().apply {
            verify(set, times(1)).add(capture())
            assertEquals(key, firstValue.key)
            assertEquals(RuleValue(rule), firstValue.value)
        }
    }
}
