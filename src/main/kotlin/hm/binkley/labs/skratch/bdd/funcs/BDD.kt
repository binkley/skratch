package hm.binkley.labs.skratch.bdd.funcs

import hm.binkley.labs.skratch.bdd.funcs.BDD.Given.When
import hm.binkley.labs.skratch.bdd.funcs.BDD.Given.When.Then
import hm.binkley.labs.skratch.bdd.funcs.BDD.Given.When.Then.Qed
import jdk.nashorn.internal.runtime.Debug.caller

data class BDD(val GIVEN: String, val WHEN: String,
               val THEN: String = caller()) {
    override fun toString() = "GIVEN $GIVEN WHEN $WHEN THEN $THEN"

    companion object {
        val GIVEN = Given()
        val WHEN = GIVEN.When()
        val THEN = WHEN.Then()
        val QED = THEN.Qed()

        // `caller` is inline to preserve the stack trace
        inline private fun caller() = Throwable().stackTrace[1].methodName!!

        inline fun upon(GIVEN: Given, WHEN: When, action: () -> Unit): When {
            action.invoke()
            return GIVEN.When()
        }

        inline fun upon(WHEN: When, THEN: Then, action: () -> Unit): Then {
            action.invoke()
            return WHEN.Then()
        }

        inline fun upon(THEN: Then, QED: Qed, action: () -> Unit): BDD {
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
