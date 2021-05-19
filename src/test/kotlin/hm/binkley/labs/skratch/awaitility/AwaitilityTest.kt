package hm.binkley.labs.skratch.awaitility

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit.SECONDS

@Suppress("DeferredResultUnused")
internal class AwaitilityTest {
    var x = 0

    @BeforeEach
    fun setUp() {
        x = 0
    }

    @Test
    fun shouldWait() {
        slow()
        await().atMost(1L, SECONDS).until {
            x == 1
        }
    }

    private fun slow() = runBlocking {
        async {
            delay(100L)
            x = 1
        }
    }
}
