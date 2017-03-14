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

fun main(args: Array<String>) {
    println(GIVEN `an apple`
            WHEN `it falls`
            THEN `Newton thinks`
            QED)
}

var apple: Apple? = null

infix fun Given.`an apple`(WHEN: When): When {
    apple = Apple(
            Newton(thinking = false))
    return When()
}

infix fun When.`it falls`(THEN: Then): Then {
    apple?.fall()
    return Then()
}

infix fun Then.`Newton thinks`(QED: Qed): BDD {
    assert(apple?.physicist?.thinking ?: false) {
        "Newton is sleeping"
    }
    return BDD(GIVEN, WHEN)
}

inline fun caller() = Throwable().stackTrace[1].methodName

data class BDD(val GIVEN: String, val WHEN: String,
        val THEN: String = caller()) {
    companion object {
        val GIVEN = Given()
        val WHEN = GIVEN.When()
        val THEN = WHEN.Then()
        val QED = THEN.Qed()
    }

    class Given {
        inner class When(val GIVEN: String = caller()) {
            inner class Then(val GIVEN: String = this@When.GIVEN,
                    val WHEN: String = caller()) {
                inner class Qed
            }
        }
    }
}
