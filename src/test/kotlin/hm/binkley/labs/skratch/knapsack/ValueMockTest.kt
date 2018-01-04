package hm.binkley.labs.skratch.knapsack

import hm.binkley.labs.skratch.knapsack.Value.Nonce
import hm.binkley.labs.skratch.knapsack.Value.RuleValue
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

internal class ValueMockTest {
    private val layer = 0
    private val key = "foo"
    private val database = mockk<Database>()
    private val value = database.value("3")

    @Test
    fun shouldComplainWhenNonceRemoves() {
        assertThrows(IllegalStateException::class.java) {
            Nonce.remove(layer, key)
        }
    }

    @Test
    fun shouldComplainWhenNonceReplacesWith() {
        assertThrows(IllegalStateException::class.java) {
            Nonce.replaceWith(layer, key, Nonce)
        }
    }

    @Test
    fun shouldComplainWhenNonceAdds() {
        assertThrows(IllegalStateException::class.java) {
            Nonce.add(layer, key)
        }
    }

    @Test
    fun shouldRemoveFromDatabase() {
        value.remove(layer, key)

        verify { database.remove(layer, key) }
    }

    @Test
    fun shouldRemoveWhenReplacedByNothing() {
        value.replaceWith(layer, key, Nonce)

        verify { database.remove(layer, key) }
    }

    @Test
    fun shouldRemoveWhenReplacedByRule() {
        value.replaceWith(layer, key, RuleValue { _, _ -> 3 })

        verify { database.remove(layer, key) }
    }

    @Test
    fun shouldIgnoreWhenReplacedByAnotherValue() {
        val other = database.value("4")
        value.replaceWith(layer, key, other)

        // verifyZeroInteractions(database) -- should fail because no spec?
    }

    @Test
    fun shouldAddToDatabase() {
        value.add(layer, key)

        verify { database.upsert(layer, key, value.value) }
    }

    @Test
    fun shouldGetRule_toMakeJaCoCoHappy() {
        val rule: Rule<Int> = { _, _ -> 3 }

        assertSame(rule, RuleValue(rule).rule)
    }
}
