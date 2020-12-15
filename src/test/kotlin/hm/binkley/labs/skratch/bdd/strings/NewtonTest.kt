@file:Suppress("LocalVariableName", "ObjectPropertyName")

package hm.binkley.labs.skratch.bdd.strings

import hm.binkley.labs.skratch.bdd.Apple
import hm.binkley.labs.skratch.bdd.Newton
import hm.binkley.labs.skratch.bdd.strings.BDD.Companion.SO
import hm.binkley.labs.skratch.bdd.strings.BDD.Companion.upon
import org.junit.jupiter.api.Test

internal class NewtonTest {
    private lateinit var apple: Apple

    @Test
    fun `should think`() {
        val Thinking = "Thinking"
        upon(`an apple`) {
            apple = Apple(Newton(thinking = false))
        }
        upon(`it falls`) {
            apple.falls()
        }
        upon(`Newton thinks`) {
            assert(apple.physicist.thinking) {
                "Newton is sleeping"
            }
        }

        // TODO: Breaking these across lines confuses Kotlin compiler
        SO SCENARIO Thinking GIVEN `an apple` WHEN `it falls` THEN `Newton thinks`
    }

    companion object {
        private const val `an apple` = "an apple"
        private const val `it falls` = "it falls"
        private const val `Newton thinks` = "Newton thinks"
    }
}
