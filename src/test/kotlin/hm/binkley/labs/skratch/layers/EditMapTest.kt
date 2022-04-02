package hm.binkley.labs.skratch.layers

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.maps.shouldNotHaveKey
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

internal class EditMapTest {
    private val editMap = TestEditMap()

    @Test
    fun `should put unwrapped values`() {
        editMap["BOB"] = 17

        editMap.map["BOB"] shouldBe 17.toValue()
    }

    @Test
    fun `should edit by delegate`() {
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
        editMap["BOB"] = editMap.rule<Int>("BOB") { _, _, _ -> null }

        shouldThrow<NotAValueException> {
            ++editMap.BOB
        }
    }

    @Test
    fun `should complain when editing by delegate for missing key`() {
        shouldThrow<MissingKeyException> {
            editMap.BOB
        }
    }
}

private var TestEditMap.BOB: Int by EditMapDelegate { name }
private var TestEditMap.NANCY: Int? by EditMapDelegate { name }
