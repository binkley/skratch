package hm.binkley.labs.skratch.collections

import com.nhaarman.mockito_kotlin.mock
import hm.binkley.labs.skratch.collections.Value.RuleValue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class LayersMockTest {
    private val database: Database = mock()

    @Test
    fun shouldStartEmpty() {
        assertTrue(Layers().isEmpty())
    }

    @Test
    fun shouldStartZeroSized() {
        assertEquals(0, Layers().size)
    }

    @Test
    fun shouldFetchAllNonNullDatabaseValuesForKey() {
        val mapA = ValueMap(database, 0)
        mapA["foo"] = "3"
        val mapB = ValueMap(database, 1)
        val mapC = ValueMap(database, 2)
        mapC["foo"] = "4"
        val mapD = ValueMap(database, 3)
        mapD["foo"] = RuleValue { _, _ -> 3 }
        val layers = Layers(mutableListOf(mapA, mapB, mapC, mapD))

        assertEquals(layers["foo"], listOf("3", "4"))
    }
}
