@file:Suppress("TestFunctionName")

package hm.binkley.labs.skratch.bdd.funcs

import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.GIVEN
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.QED
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.SCENARIO
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.THEN
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.WHEN
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/**
 * @todo Note that _colors and formatting_ do not show up in these tests
 *       System Lambda uses reflection to edit the envvars, and recent JDK
 *       versions prevent this
 */
internal class NewtonTest {
    @Test
    fun `should think (happy path)`() {
        val it =
            SCENARIO `A revolution begins`
                GIVEN `an apple`
                WHEN `it falls`
                THEN `Newton thinks`
                QED

        val scenarioText =
            """
            ✓ SCENARIO A revolution begins
                GIVEN an apple
                WHEN it falls
                THEN Newton thinks
            """.trimIndent()

        assert("$it" == scenarioText) {
            "Wrong message in passed scenario: Expected:\n$scenarioText\nGot:\n$it"
        }
    }

    @Test
    fun `should have broken production code for eating fallen apple (failed test)`() {
        val e =
            assertThrows<AssertionError> {
                SCENARIO `A snack helps with thinking`
                    GIVEN `an apple`
                    WHEN `it falls`
                    THEN `Newton eats the apple`
                    QED
            }

        val scenarioText =
            """
            ✗ SCENARIO A snack helps with thinking
                GIVEN an apple
                WHEN it falls
                THEN Newton eats the apple
            """.trimIndent()

        assert(
            e.message == "Failed THEN clause in:\n$scenarioText\njava.lang.AssertionError: Newton should be eating the apple while thinking"
        ) {
            "Wrong message in failed scenario: Expected:\n$scenarioText\nGot:\n${e.message}"
        }
    }

    @Test
    fun `should not sleep (broken test)`() {
        val e =
            assertThrows<IllegalStateException> {
                SCENARIO `A revolution is missed`
                    GIVEN `an apple`
                    WHEN `it falls`
                    THEN `Newton sleeps`
                    QED
            }

        val scenarioText =
            """
            ‽ SCENARIO A revolution is missed
                GIVEN an apple
                WHEN it falls
                THEN Newton sleeps
            """.trimIndent()

        assert(
            e.message == "Errored THEN clause in:\n$scenarioText\njava.lang.IllegalStateException: Newton should be thinking, not sleeping"
        ) {
            "Wrong message in failed scenario: Expected:\n$scenarioText\nGot:\n${e.message}"
        }
    }
}
