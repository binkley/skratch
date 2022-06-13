package hm.binkley.labs.skratch.async

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class GeneratorTest {
    // @CoroutinesTimeout(1_000) -- TODO: Wonky maven dep problem with 1.6.2
    @Test
    fun `should generate`() {
        val seq = sequence {
            yield(1)
            yield(2)
        }

        seq.toList() shouldBe listOf(1, 2)
    }
}
