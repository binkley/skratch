package hm.binkley.labs.skratch.bdd.funcs

import hm.binkley.labs.skratch.bdd.funcs.BDD.Given.When
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
    }

    class Given {
        inline fun upon(WHEN: When, action: () -> Unit): When {
            action.invoke()
            return this.When()
        }

        inner class When(val GIVEN: String = caller()) {
            inline fun upon(THEN: Then, action: () -> Unit): Then {
                action.invoke()
                return this.Then()
            }

            inner class Then(val GIVEN: String = this@When.GIVEN,
                             val WHEN: String = caller()) {
                inline fun upon(QED: Qed, action: () -> Unit): BDD {
                    action.invoke()
                    return BDD(this.GIVEN, this.WHEN)
                }

                inner class Qed
            }
        }
    }
}
