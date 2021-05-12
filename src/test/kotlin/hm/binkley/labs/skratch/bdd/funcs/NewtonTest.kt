@file:Suppress("TestFunctionName")

package hm.binkley.labs.skratch.bdd.funcs

import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.GIVEN
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.QED
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.SCENARIO
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.THEN
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.WHEN
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class NewtonTest {
    @Test
    fun `should think`() {
        val it = SCENARIO `A revolution begins`
                GIVEN `an apple`
                WHEN `it falls`
                THEN `Newton thinks`
                QED

        val expected = """
            SCENARIO A revolution begins
                GIVEN an apple
                WHEN it falls
                THEN Newton thinks
        """.trimIndent()

        assert("$it" == expected) {
            "Expected: `SCENARIO A revolution begins: GIVEN an apple WHEN it falls THEN Newton thinks`, got: `$it`"
        }
    }

    @Test
    fun `should not sleep`() {
        val e = assertThrows<AssertionError> {
            SCENARIO `A revolution is missed`
                    GIVEN `an apple`
                    WHEN `it falls`
                    THEN `Newton sleeps`
                    QED
        }

        val expected = """
            SCENARIO A revolution is missed
                GIVEN an apple
                WHEN it falls
                THEN Newton sleeps
        """.trimIndent()

        assert(e.message == "Failed THEN in $expected: java.lang.AssertionError: Newton is still thinking") {
            "Expected: `Newton is still thinking`; got: `${e.message}`"
        }
    }
}
