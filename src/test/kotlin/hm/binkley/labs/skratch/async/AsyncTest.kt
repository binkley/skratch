package hm.binkley.labs.skratch.async

import kotlinx.coroutines.async
import kotlinx.coroutines.debug.junit5.CoroutinesTimeout
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

@Suppress("DeferredResultUnused")
internal class AsyncTest {
    var x = 0

    @CoroutinesTimeout(1_000)
    @Test
    fun `should timeout`() {
        slow()

        assertEquals(1, x)
    }

    private fun slow() = runBlocking {
        async {
            delay(100)
            x = 1
        }
    }
}
