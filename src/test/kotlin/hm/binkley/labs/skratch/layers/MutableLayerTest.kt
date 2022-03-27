package hm.binkley.labs.skratch.layers

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

internal class MutableLayerTest {
    @Test
    fun `should edit self`() {
        val layer = TestLayer()

        layer.edit { this["BOB"] = 17 }

        layer["BOB"] shouldBe 17.toValue()
    }

    @Test
    fun `should defensive copy`() {
        val map = mutableMapOf("BOB" to 17.toValue())
        val layer = TestLayer(map)

        layer shouldBe map

        layer.edit { clear() }

        layer shouldNotBe map

        map.clear()

        layer shouldBe map
    }
}
