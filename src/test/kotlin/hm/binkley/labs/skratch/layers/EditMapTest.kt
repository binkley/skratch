package hm.binkley.labs.skratch.layers

import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class EditMapTest {
    @Test
    fun `should put unwrapped values`() {
        val editMap = TestEditMap()

        editMap["BOB"] = 17

        editMap.map["BOB"] shouldBe Value(17)
    }

    @Test
    fun `should create anonymous rules`() {
        val editMap = TestEditMap()

        val rule = editMap.rule<Int>("BOB") {
                _: String, _: Sequence<Int>, _: Layers<String, Int, *> ->
            null
        }

        rule("BOB", emptySequence(), TestLayers()).shouldBeNull()
    }
}

private class TestEditMap(
    val map: MutableMap<String, ValueOrRule<Int>> = mutableMapOf(),
) : EditMap<String, Int>, MutableMap<String, ValueOrRule<Int>> by map
