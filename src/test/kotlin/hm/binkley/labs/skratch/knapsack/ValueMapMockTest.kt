package hm.binkley.labs.skratch.knapsack

import hm.binkley.labs.skratch.knapsack.Value.Nonce
import hm.binkley.labs.skratch.knapsack.Value.RuleValue
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class ValueMapMockTest {
    private val database = mockk<Database>()
    private val layer = 0
    private val key = "foo"

    @Test
    fun shouldGetLayer() {
        assertEquals(layer, ValueMap(database, layer, ValueSet(layer)).layer)
    }

    @Test
    fun shouldForwardWhenSettingNonce() {
        val set = spyk(ValueSet(layer))
        val element = slot<ValueEntry>()
        every { set.add(capture(element)) } returns true
        val map = ValueMap(database, layer, set)

        map[key] = null

        verify(exactly = 1) { set.add(element.captured) }
        assertEquals(key, element.captured.key)
        assertSame(Nonce, element.captured.value)
    }

    @Test
    fun shouldForwardWhenSettingValue() {
        val value = "3"
        val set = spyk(ValueSet(layer))
        val element = slot<ValueEntry>()
        every { set.add(capture(element)) } returns true
        val map = ValueMap(database, layer, set)

        map[key] = value

        verify(exactly = 1) { set.add(element.captured) }
        assertEquals(key, element.captured.key)
        assertSame(database.value(value), element.captured.value)
    }

    @Test
    fun shouldForwardWhenSettingRule() {
        val rule: Rule<Int> = { _, _ -> 3 }
        val set = spyk(ValueSet(layer))
        val element = slot<ValueEntry>()
        every { set.add(capture(element)) } returns true
        val map = ValueMap(database, layer, set)

        map[key] = rule

        verify(exactly = 1) { set.add(element.captured) }
        assertEquals(key, element.captured.key)
        assertEquals(RuleValue(rule), element.captured.value)
    }

    @Test
    fun shouldForwardWhenRemoving() {
        val entry = ValueEntry(key, RuleValue { _, _ -> 3 })
        val dummy = mutableSetOf(entry)
        val set = spyk(ValueSet(layer, mutableSetOf(entry)))
        every { set.iterator() } returns dummy.iterator()
        val map = ValueMap(database, layer, set)

        map.remove(key)

        assertTrue(dummy.isEmpty())
    }
}
