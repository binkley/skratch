package hm.binkley.labs.skratch.layers

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

internal class MutableLayerTest {
    @Test
    fun `should edit self`() {
        val layer = TestLayer(0)

        layer.edit { this["BOB"] = 17 }

        layer["BOB"] shouldBe 17.toValue()
    }

    @Test
    fun `should defensive copy for constructor`() {
        val map = mutableMapOf("BOB" to 17.toValue())
        val layer = TestLayer(0, map)

        layer shouldBe map

        layer.edit { clear() }

        layer shouldNotBe map

        map.clear()

        layer shouldBe map
    }

    @Test
    fun `should defensive copy for copy`() {
        val map = mapOf("BOB" to 17.toValue())
        val oldLayer = TestLayer(0, map)
        val layer = oldLayer.copy()

        layer shouldBe map

        oldLayer.edit { clear() }

        layer shouldBe map
    }
}
