package hm.binkley.labs.skratch.layers.rules

import hm.binkley.labs.skratch.layers.TestLayers
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class ConstantRuleTest {
    @Test
    fun `should work`() {
        val layers = TestLayers()

        layers.push { this["BOB"] = constantRule(17) }
        layers.push { this["BOB"] = 3 }

        layers["BOB"] shouldBe 17
    }
}
