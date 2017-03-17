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

var apple: Apple? = null

infix fun Given.`an apple`(WHEN: When) = BDD.upon(this) {
    apple = Apple(Newton(thinking = false))
}

infix fun When.`it falls`(THEN: Then) = BDD.upon(this) {
    apple?.falls()
}

infix fun Then.`Newton thinks`(QED: Qed) = BDD.upon(this) {
    assert(apple?.physicist?.thinking ?: false) {
        "Newton is sleeping"
    }
}

class newtonTest {
    @Test
    fun shouldThink() {
        GIVEN `an apple`
                WHEN `it falls`
                THEN `Newton thinks`
                QED
    }
}
