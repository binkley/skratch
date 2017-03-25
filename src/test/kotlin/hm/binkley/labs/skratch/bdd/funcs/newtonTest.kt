package hm.binkley.labs.skratch.bdd.funcs

import hm.binkley.labs.skratch.bdd.Apple
import hm.binkley.labs.skratch.bdd.Newton
import hm.binkley.labs.skratch.bdd.funcs.BDD.Companion.GIVEN
import hm.binkley.labs.skratch.bdd.funcs.BDD.Companion.QED
import hm.binkley.labs.skratch.bdd.funcs.BDD.Companion.THEN
import hm.binkley.labs.skratch.bdd.funcs.BDD.Companion.WHEN
import hm.binkley.labs.skratch.bdd.funcs.BDD.Given
import hm.binkley.labs.skratch.bdd.funcs.BDD.Given.When
import hm.binkley.labs.skratch.bdd.funcs.BDD.Given.When.Then
import hm.binkley.labs.skratch.bdd.funcs.BDD.Given.When.Then.Qed
import org.junit.jupiter.api.Test

class newtonTest {
    lateinit var apple: Apple

    infix fun Given.`an apple`(WHEN: When) = this.upon {
        apple = Apple(Newton(thinking = false))
    }

    infix fun When.`it falls`(THEN: Then) = this.upon {
        apple.falls()
    }

    infix fun Then.`Newton thinks`(QED: Qed) = this.upon {
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
        assert(bdd.toString() ==
                "GIVEN an apple WHEN it falls THEN Newton thinks") {
            "Expected: `GIVEN an apple WHEN it falls THEN Newton thinks`, got: $bdd"
        }
    }
}
