package hm.binkley.labs.skratch.async

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.debug.junit5.CoroutinesTimeout
import org.junit.jupiter.api.Test

internal class GeneratorTest {
    @CoroutinesTimeout(1_000)
    @Test
    fun `should generate`() {
        val seq = sequence {
            yield(1)
            yield(2)
        }

        seq.toList() shouldBe listOf(1, 2)
    }
}
