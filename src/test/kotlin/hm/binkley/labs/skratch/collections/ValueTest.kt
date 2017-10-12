package hm.binkley.labs.skratch.collections

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import hm.binkley.labs.skratch.collections.Value.DatabaseValue
import hm.binkley.labs.skratch.collections.Value.Nonce
import hm.binkley.labs.skratch.collections.Value.RuleValue
import org.junit.jupiter.api.Test

internal class ValueTest {
    private val layer = 0
    private val key = "foo"
    private val database: Database = mock()
    val value = DatabaseValue(database, "3")

    @Test
    fun shouldRemoveFromDatabase() {
        value.remove(layer, key)

        verify(database).remove(layer, key)
    }

    @Test
    fun shouldRemoveWhenReplacedByNothing() {
        value.replaceWith(layer, key, Nonce)

        verify(database).remove(layer, key)
    }

    @Test
    fun shouldRemoveWhenReplacedByRule() {
        value.replaceWith(layer, key, RuleValue({ _, _ -> 3 }))

        verify(database).remove(layer, key)
    }

    @Test
    fun shouldIgnoreWhenReplacedByAnotherValue() {
        val other = DatabaseValue(database, "4")
        value.replaceWith(layer, key, other)

        verifyZeroInteractions(database)
    }

    @Test
    fun shouldAddToDatabase() {
        value.add(layer, key)

        verify(database).upsert(layer, key, value.value)
    }
}
