package hm.binkley.labs.skratch.layers

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class MutableLayerTest {
    @Test
    fun `should edit self`() {
        val layer = TestLayer()

        layer.edit { this["BOB"] = 17 }

        layer["BOB"] shouldBe 17.toValue()
    }
}
