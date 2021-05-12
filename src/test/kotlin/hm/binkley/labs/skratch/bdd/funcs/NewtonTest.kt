@file:Suppress("TestFunctionName", "UNUSED_PARAMETER")

package hm.binkley.labs.skratch.bdd.funcs

import hm.binkley.labs.skratch.bdd.Apple
import hm.binkley.labs.skratch.bdd.Newton
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.GIVEN
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.QED
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.SCENARIO
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.THEN
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.WHEN
import hm.binkley.labs.skratch.bdd.funcs.QED.Given
import hm.binkley.labs.skratch.bdd.funcs.QED.Scenario
import hm.binkley.labs.skratch.bdd.funcs.QED.Then
import hm.binkley.labs.skratch.bdd.funcs.QED.When
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class NewtonTest {
    private lateinit var apple: Apple

    // The scenario extension functions need to be:
    // 1. Non-local functions
    // 2. Infix
    // This lets the framework handle G-W-T correctly, and spells nicely

    private infix fun Scenario.`A revolution begins`(GIVEN: Given) = act { }

    @Test
    fun `should think`() {
        val it = SCENARIO `A revolution begins`
                GIVEN `an apple`
                WHEN `it falls`
                THEN `Newton thinks`
                QED

        assert("$it" == "SCENARIO A revolution begins: GIVEN an apple WHEN it falls THEN Newton thinks") {
            "Expected: `SCENARIO A revolution begins: GIVEN an apple WHEN it falls THEN Newton thinks`, got: `$it`"
        }
    }

    private infix fun Scenario.`A revolution is missed`(GIVEN: Given) =
        act { }

    @Test
    fun `should not sleep`() {
        val e = assertThrows<AssertionError> {
            SCENARIO `A revolution is missed`
                    GIVEN `an apple`
                    WHEN `it falls`
                    THEN `Newton sleeps`
                    QED
        }

        assert(e.message == "Failed THEN in SCENARIO A revolution is missed: GIVEN an apple WHEN it falls THEN Newton sleeps: java.lang.AssertionError: Newton is still thinking") {
            "Expected: `Newton is still thinking`; got: `${e.message}`"
        }
    }

    private infix fun Given.`an apple`(WHEN: When) = act {
        apple = Apple(Newton(thinking = false))
    }

    private infix fun When.`it falls`(THEN: Then) = act {
        apple.falls()
    }

    private infix fun Then.`Newton thinks`(QED: QED) = act {
        assert(apple.physicist.thinking) {
            "Newton is still sleeping"
        }
    }

    private infix fun Then.`Newton sleeps`(QED: QED) = act {
        assert(!apple.physicist.thinking) {
            "Newton is still thinking"
        }
    }
}
