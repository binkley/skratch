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

class newtonTest {
    lateinit private var apple: Apple

    infix fun Given.`an apple`(WHEN: When) = upon {
        apple = Apple(Newton(thinking = false))
    }

    infix fun When.`it falls`(THEN: Then) = upon {
        apple.falls()
    }

    infix fun Then.`Newton thinks`(QED: Qed) = upon {
        assert(apple.physicist.thinking) {
            "Newton is sleeping"
        }
    }

    @Test
    fun shouldThink() {
        val bdd = GIVEN `an apple`
                WHEN `it falls`
                THEN `Newton thinks`
                QED
        assert("$bdd" == "GIVEN an apple WHEN it falls THEN Newton thinks") {
            "Expected: `GIVEN an apple WHEN it falls THEN Newton thinks`, got: $bdd"
        }
    }
}
