package hm.binkley.labs.skratch.awaitility

import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.awaitility.Awaitility.await
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit.SECONDS

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
            sleep(100L)
            x = 1
        }
    }
}
