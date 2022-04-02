package hm.binkley.labs.skratch.layers

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class EditMapTest {
    @Test
    fun `should put unwrapped values`() {
        val editMap = TestEditMap()

        editMap["BOB"] = 17

        editMap.map["BOB"] shouldBe 17.toValue()
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

    @Test
    fun `should edit by delegate`() {
        val editMap = TestEditMap()

        editMap.BOB = 16
        ++editMap.BOB

        editMap.map["BOB"] shouldBe 17.toValue()
    }

    @Test
    fun `should complain when editing by delegate for a rule`() {
        val editMap = TestEditMap()

        editMap["BOB"] = editMap.rule<Int>("BOB") {
                _: String, _: Sequence<Int>, _: Layers<String, Int, *> ->
            null
        }

        shouldThrow<NotAValueException> {
            ++editMap.BOB
        }
    }

    @Test
    fun `should complain when editing by delegate for missing key`() {
        val editMap = TestEditMap()

        shouldThrow<MissingKeyException> {
            editMap.BOB
        }
    }
}

private var TestEditMap.BOB: Int by EditDelegate { name }

private class TestEditMap(
    val map: MutableMap<String, ValueOrRule<Int>> = mutableMapOf(),
) : EditMap<String, Int>, MutableMap<String, ValueOrRule<Int>> by map
