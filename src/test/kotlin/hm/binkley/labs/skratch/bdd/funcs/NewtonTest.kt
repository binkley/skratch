@file:Suppress("TestFunctionName", "UNUSED_PARAMETER")

package hm.binkley.labs.skratch.bdd.funcs

import hm.binkley.labs.skratch.bdd.Apple
import hm.binkley.labs.skratch.bdd.Newton
import hm.binkley.labs.skratch.bdd.funcs.Qed.Companion.GIVEN
import hm.binkley.labs.skratch.bdd.funcs.Qed.Companion.QED
import hm.binkley.labs.skratch.bdd.funcs.Qed.Companion.SCENARIO
import hm.binkley.labs.skratch.bdd.funcs.Qed.Companion.THEN
import hm.binkley.labs.skratch.bdd.funcs.Qed.Companion.WHEN
import hm.binkley.labs.skratch.bdd.funcs.Qed.Given
import hm.binkley.labs.skratch.bdd.funcs.Qed.Scenario
import hm.binkley.labs.skratch.bdd.funcs.Qed.Then
import hm.binkley.labs.skratch.bdd.funcs.Qed.When
import org.junit.jupiter.api.Test

internal class NewtonTest {
    private lateinit var apple: Apple

    private infix fun Given.`an apple`(WHEN: When) = act {
        apple = Apple(Newton(thinking = false))
    }

    private infix fun When.`it falls`(THEN: Then) = act {
        apple.falls()
    }

    private infix fun Then.`Newton thinks`(QED: Qed) = act {
        assert(apple.physicist.thinking) {
            "Newton is still sleeping"
        }
    }

    private infix fun Scenario.Thinking(GIVEN: Given) = act { }

    @Test
    fun `should think`() {
        val it = SCENARIO Thinking
                GIVEN `an apple`
                WHEN `it falls`
                THEN `Newton thinks`
                QED
        assert("$it" == "SCENARIO Thinking: GIVEN an apple WHEN it falls THEN Newton thinks") {
            "Expected: `SCENARIO Thinking: GIVEN an apple WHEN it falls THEN Newton thinks`, got: $it"
        }
    }

    private infix fun Scenario.Mamboing(GIVEN: Given) = act { }

    @Test
    fun `should mambo`() = scenario {
        -(SCENARIO Mamboing
                GIVEN `an apple`
                WHEN `it falls`
                THEN `Newton thinks`
                QED)

        assert("$it" == "SCENARIO Mamboing: GIVEN an apple WHEN it falls THEN Newton thinks") {
            "Expected: `SCENARIO Mamboing: GIVEN an apple WHEN it falls THEN Newton thinks`, got: $it"
        }
    }

    private fun scenario(test: GWT.() -> Unit) = GWT().let(test)

    class GWT {
        lateinit var it: Qed
        operator fun Qed.unaryMinus() {
            it = this
        }
    }
}
