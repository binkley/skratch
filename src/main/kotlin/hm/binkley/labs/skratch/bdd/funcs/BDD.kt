package hm.binkley.labs.skratch.bdd.funcs

import hm.binkley.labs.skratch.bdd.Apple
import hm.binkley.labs.skratch.bdd.Newton
import hm.binkley.labs.skratch.bdd.funcs.BDD.Companion.GIVEN
import hm.binkley.labs.skratch.bdd.funcs.BDD.Companion.QED
import hm.binkley.labs.skratch.bdd.funcs.BDD.Companion.THEN
import hm.binkley.labs.skratch.bdd.funcs.BDD.Companion.WHEN
import hm.binkley.labs.skratch.bdd.funcs.BDD.Companion.upon
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

infix fun Given.`an apple`(WHEN: When) = upon(this) {
    apple = Apple(Newton(thinking = false))
}

infix fun When.`it falls`(THEN: Then) = upon(this) {
    apple?.falls()
}

infix fun Then.`Newton thinks`(QED: Qed) = upon(this) {
    assert(apple?.physicist?.thinking ?: false) {
        "Newton is sleeping"
    }
}

inline fun caller() = Throwable().stackTrace[1].methodName

data class BDD(val GIVEN: String, val WHEN: String,
        val THEN: String = caller()) {
    companion object {
        val GIVEN = Given()
        val WHEN = GIVEN.When()
        val THEN = WHEN.Then()
        val QED = THEN.Qed()

        inline fun upon(GIVEN: Given, action: () -> Unit): When {
            action.invoke()
            return GIVEN.When()
        }

        inline fun upon(WHEN: When, action: () -> Unit): Then {
            action.invoke()
            return WHEN.Then()
        }

        inline fun upon(THEN: Then, action: () -> Unit): BDD {
            action.invoke()
            return BDD(THEN.GIVEN, THEN.WHEN)
        }
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
