package hm.binkley.labs.skratch.layers

import hm.binkley.labs.skratch.layers.rules.LastOrDefaultRule
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class MutableLayersTest {
    @Test
    fun `should rollback and complain for bad edits`() {
        val map = mapOf("BOB" to LastOrDefaultRule<String, Int, Int>(17))
        val layers = TestLayers { putAll(map) }

        shouldThrow<MissingRuleException> {
            layers.edit {
                this["NANCY"] = 3
            }
        }

        layers.history shouldBe listOf(map)
    }
}
