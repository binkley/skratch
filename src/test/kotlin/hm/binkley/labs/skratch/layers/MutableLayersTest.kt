package hm.binkley.labs.skratch.layers

import hm.binkley.labs.skratch.layers.rules.constantRule
import hm.binkley.labs.skratch.layers.rules.lastOrDefaultRule
import hm.binkley.labs.skratch.layers.rules.lastOrNullRule
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class MutableLayersTest {
    @Test
    fun `should distinguish mutable from immutable`() {
        val layers = TestLayers()
        layers.edit { } // should compile
        // (layers as Layers<String, Int, *>).edit() // no compile
    }

    @Test
    fun `should complain for bad first layer`() {
        val badMap = mapOf("BOB" to 17.toValue())

        shouldThrow<MissingFirstLayerException> {
            TestLayers(emptyList())
        }
        shouldThrow<MissingRuleException> {
            TestLayers(TestLayer(0, badMap))
        }
        shouldThrow<MissingRuleException> {
            TestLayers { putAll(badMap) }
        }
    }

    @Test
    fun `should get-as type with excluded layers`() {
        val layers = TestLayers {
            this["BOB"] = lastOrDefaultRule(17)
            this["NANCY"] = lastOrDefaultRule(3)
        }
        val layer = layers.push {
            this["BOB"] = constantRule(3)
            NANCY = 4
        }

        val nancy: Int? = layers.getAs("NANCY", listOf(layer))

        nancy shouldBe 3
    }

    @Test
    fun `should rollback and complain for bad edits`() {
        val layers = TestLayers {
            this["BOB"] = lastOrDefaultRule(17)
        }

        layers.shouldRollback<MissingRuleException> {
            it.edit {
                BOB = 3
            }
        }
    }

    @Test
    fun `should hide keys`() {
        val layers = TestLayers { this["BOB"] = lastOrNullRule() }

        layers.shouldBeEmpty()
    }

    @Test
    fun `should hide delegated keys`() {
        val layers = TestLayers { this["BOB"] = lastOrDefaultRule(3) }

        layers.edit {
            BOB = null
        }

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
            layers.whatIf { BOB = 17 }
        }
    }

    @Suppress("DANGEROUS_CHARACTERS")
    @Test
    fun `should ask what if not?`() {
        val layers = TestLayers {
            this["BOB"] = lastOrDefaultRule(17)
            this["NANCY"] = lastOrDefaultRule(3)
        }
        val layer = layers.push {
            this["BOB"] = constantRule(3)
            NANCY = 4
        }

        val whatIfNot = layers.whatIfNot(layer)

        whatIfNot shouldBe mapOf("BOB" to 17, "NANCY" to 3)
        whatIfNot.history.size shouldBe (layers.history.size - 1)

        layers.shouldRollback<MissingRuleException> {
            // TODO: Avoid casting
            layers.whatIfNot(layers.history.first() as TestLayer)
        }
    }

    @Test
    fun `should pop`() {
        val layers = TestLayers(
            TestLayer(0) { this["BOB"] = lastOrDefaultRule(17) },
            TestLayer(1) { BOB = 3 }
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
            layers.push { NANCY = 3 }
        }
    }
}
