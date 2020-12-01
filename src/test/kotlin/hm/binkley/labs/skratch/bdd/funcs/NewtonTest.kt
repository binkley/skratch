package hm.binkley.labs.skratch.bdd.funcs

import hm.binkley.labs.skratch.bdd.Apple
import hm.binkley.labs.skratch.bdd.Newton
import hm.binkley.labs.skratch.bdd.funcs.Qed.Companion.GIVEN
import hm.binkley.labs.skratch.bdd.funcs.Qed.Companion.QED
import hm.binkley.labs.skratch.bdd.funcs.Qed.Companion.THEN
import hm.binkley.labs.skratch.bdd.funcs.Qed.Companion.WHEN
import hm.binkley.labs.skratch.bdd.funcs.Qed.Given
import hm.binkley.labs.skratch.bdd.funcs.Qed.Then
import hm.binkley.labs.skratch.bdd.funcs.Qed.When
import org.junit.jupiter.api.Test

internal class NewtonTest {
    private lateinit var apple: Apple

    @Suppress("UNUSED_PARAMETER")
    infix fun Given.`an apple`(WHEN: When) = act {
        apple = Apple(Newton(thinking = false))
    }

    @Suppress("UNUSED_PARAMETER")
    infix fun When.`it falls`(THEN: Then) = act {
        apple.falls()
    }

    @Suppress("UNUSED_PARAMETER")
    infix fun Then.`Newton thinks`(QED: Qed) = act {
        assert(apple.physicist.thinking) {
            "Newton is still sleeping"
        }
    }

    @Test
    fun `should think`() {
        val bdd = GIVEN `an apple`
                WHEN `it falls`
                THEN `Newton thinks`
                QED
        assert("$bdd" == "GIVEN an apple WHEN it falls THEN Newton thinks") {
            "Expected: `GIVEN an apple WHEN it falls THEN Newton thinks`, got: $bdd"
        }
    }

    @Test
    fun `should mamba`() = scenario {
        -(GIVEN `an apple` WHEN `it falls` THEN `Newton thinks` QED)

        assert("$it" == "GIVEN an apple WHEN it falls THEN Newton thinks") {
            "Expected: `GIVEN an apple WHEN it falls THEN Newton thinks`, got: $it"
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
