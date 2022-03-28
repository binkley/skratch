package hm.binkley.labs.skratch.layers

import hm.binkley.labs.skratch.layers.rules.LastOrDefaultRule
import hm.binkley.labs.skratch.layers.rules.lastOrDefaultRule
import hm.binkley.labs.skratch.layers.rules.lastOrNullRule
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldNotBeEmpty
import io.kotest.matchers.shouldBe
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

    @Test
    fun `should undo`() {
        val layers = TestLayers()

        layers.push { this["BOB"] = lastOrDefaultRule(17) }

        layers.shouldNotBeEmpty()

        layers.pop()

        layers.shouldBeEmpty()
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
        val initHistory = layers.history.toList() // Defensive copy

        val whatIf = layers.whatIf {
            this["BOB"] = lastOrDefaultRule(3)
        }

        whatIf shouldBe mapOf("BOB" to 3, "NANCY" to 3)
        whatIf.history.size shouldBe layers.history.size
        layers.history shouldBe initHistory

        shouldThrow<LayersException> {
            layers.whatIf { this["BOB"] = 17 }
        }
        layers.history shouldBe initHistory // rollback preserves
    }

    @Test
    fun `should complain when popping initial rules`() {
        val layers = TestLayers { this["BOB"] = lastOrDefaultRule(17) }
        val initHistory = layers.history.toList() // Defensive copy

        shouldThrow<MissingFirstLayerException> {
            layers.pop()
        }
        layers.history shouldBe initHistory // rollback preserves
    }

    @Test
    fun `should complain when pushing a bad layer`() {
        val layers = TestLayers { this["BOB"] = lastOrDefaultRule(17) }
        val initHistory = layers.history.toList() // Defensive copy

        shouldThrow<MissingRuleException> {
            layers.push { this["NANCY"] = 3 }
        }
        layers.history shouldBe initHistory // rollback preserves
    }
}
