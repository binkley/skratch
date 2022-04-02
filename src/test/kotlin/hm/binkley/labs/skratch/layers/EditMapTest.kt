package hm.binkley.labs.skratch.layers

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.maps.shouldNotHaveKey
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
    fun `should edit by delegate`() {
        val editMap = TestEditMap()

        editMap.BOB = 16
        ++editMap.BOB

        editMap.map["BOB"] shouldBe 17.toValue()
    }

    @Test
    fun `should remove by delegate`() {
        val editMap = TestEditMap(
            mutableMapOf(
                "NANCY" to 3.toValue()
            )
        )

        editMap.NANCY = null

        editMap.map shouldNotHaveKey "NANCY"
    }

    @Test
    fun `should complain when editing by delegate for a rule`() {
        val editMap = TestEditMap()

        editMap["BOB"] = editMap.rule<Int>("BOB") { _, _, _ -> null }

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

private var TestEditMap.BOB: Int by EditMapDelegate { name }
private var TestEditMap.NANCY: Int? by EditMapDelegate { name }

private class TestEditMap(
    val map: MutableMap<String, ValueOrRule<Int>> = mutableMapOf(),
) : EditMap<String, Int>, MutableMap<String, ValueOrRule<Int>> by map
