package hm.binkley.labs.skratch.bdd

import hm.binkley.labs.skratch.bdd.BDD.Companion.So

data class BDD constructor(
        val GIVEN: String, val WHEN: String, val THEN: String) {
    companion object {
        val So = So()
    }

    class So {
        infix fun GIVEN(GIVEN: String) = Given(GIVEN)
        data class Given(private val GIVEN: String) {
            infix fun WHEN(WHEN: String) = When(this, WHEN)
            data class When(private val GIVEN: Given, private val WHEN: String) {
                infix fun THEN(THEN: String)
                        = BDD(GIVEN.GIVEN, WHEN, THEN)
            }
        }
    }
}

fun main(args: Array<String>) {
    println(So
            GIVEN "an apple"
            WHEN "it falls"
            THEN "Newton thinks")
}
