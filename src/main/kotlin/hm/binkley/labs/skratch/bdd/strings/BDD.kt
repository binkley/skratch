package hm.binkley.labs.skratch.bdd.strings

import hm.binkley.labs.skratch.bdd.Apple
import hm.binkley.labs.skratch.bdd.Newton
import hm.binkley.labs.skratch.bdd.strings.BDD.Companion.So
import hm.binkley.labs.skratch.bdd.strings.BDD.Companion.upon


fun main(args: Array<String>) {
    var apple: Apple? = null
    upon("an apple") { apple = Apple(Newton(thinking = false)) }
    upon("it falls") { apple?.falls() }
    upon("Newton thinks") {
        assert(apple?.physicist?.thinking ?: false) {
            "Newton is sleeping"
        }
    }

    println(So
            GIVEN "an apple"
            WHEN "it falls"
            THEN "Newton thinks")
}

data class BDD constructor(
        val GIVEN: String, val WHEN: String, val THEN: String) {
    init {
        (actions[GIVEN] ?: throw IllegalArgumentException(
                "No GIVEN defined: $GIVEN")).invoke()
        (actions[WHEN] ?: throw IllegalArgumentException(
                "No WHEN defined: $WHEN")).invoke()
        (actions[THEN] ?: throw IllegalArgumentException(
                "No THEN defined: $THEN")).invoke()
    }

    companion object {
        val So = So()
        private val actions = HashMap<String, () -> Unit>()
        fun upon(clause: String, action: () -> Unit): Unit {
            actions[clause] = action
        }
    }

    class So {
        infix fun GIVEN(GIVEN: String) = Given(GIVEN)
        data class Given(private val GIVEN: String) {
            infix fun WHEN(WHEN: String) = When(GIVEN, WHEN)
            data class When(private val GIVEN: String,
                    private val WHEN: String) {
                infix fun THEN(THEN: String) = BDD(GIVEN, WHEN, THEN)
            }
        }
    }
}
