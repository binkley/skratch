package hm.binkley.labs.skratch.collections

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import hm.binkley.labs.skratch.collections.Value.DatabaseValue
import org.junit.jupiter.api.Test

internal class ValueTest {
    private val database: Database = mock()

    @Test
    fun shouldRemoveFromDatabase() {
        val value = DatabaseValue(database, "3")

        value.remove(0, "foo")

        verify(database).remove(0, "foo")
    }
}
