package hm.binkley.labs.skratch.layers

import hm.binkley.labs.skratch.layers.rules.lastOrDefaultRule
import hm.binkley.labs.skratch.layers.rules.lastOrNullRule
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

internal class MutableLayersTest {
    @Test
    fun `should complain for bad first layer`() {
        val badMap = mapOf("BOB" to 17.toValue())

        shouldThrow<MissingFirstLayerException> {
            TestLayers(emptyList())
        }
        shouldThrow<MissingRuleException> {
            TestLayers(TestLayer(badMap))
        }
        shouldThrow<MissingRuleException> {
            TestLayers { putAll(badMap) }
        }
    }

    @Disabled("CORRUPTS THE ROLLED BACK LAYER") // TODO
    @Test
    fun `should rollback and complain for bad edits`() {
        val layers = TestLayers {
            this["BOB"] = lastOrDefaultRule(17)
        }

        layers.shouldRollback<MissingRuleException> {
            it.edit {
                this["BOB"] = 3
            }
        }
    }

    @Test
    fun `should hide keys`() {
        val layers = TestLayers { this["BOB"] = lastOrNullRule() }

        layers.shouldBeEmpty()
    }

    @Suppress("DANGEROUS_CHARACTERS")
    @Test
    fun `should ask what if?`() {
        val layers = TestLayers {
            this["BOB"] = lastOrDefaultRule(17)
            this["NANCY"] = lastOrDefaultRule(3)
        }

        val whatIf = layers.whatIf {
            this["BOB"] = lastOrDefaultRule(3)
        }

        whatIf shouldBe mapOf("BOB" to 3, "NANCY" to 3)
        whatIf.history.size shouldBe layers.history.size

        layers.shouldRollback<MissingRuleException> {
            layers.whatIf { this["BOB"] = 17 }
        }
    }

    @Test
    fun `should pop`() {
        val layers = TestLayers(
            TestLayer { this["BOB"] = lastOrDefaultRule(17) },
            TestLayer { this["BOB"] = 3 }
        )

        layers.pop()

        layers shouldBe mapOf("BOB" to 17)
    }

    @Test
    fun `should complain when popping initial rules`() {
        val layers = TestLayers { this["BOB"] = lastOrDefaultRule(17) }

        layers.shouldRollback<MissingFirstLayerException> {
            layers.pop()
        }
    }

    @Test
    fun `should complain when pushing a bad layer`() {
        val layers = TestLayers { this["BOB"] = lastOrDefaultRule(17) }

        layers.shouldRollback<MissingRuleException> {
            layers.push { this["NANCY"] = 3 }
        }
    }
}
