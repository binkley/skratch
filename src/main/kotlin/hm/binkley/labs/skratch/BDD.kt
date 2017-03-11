package hm.binkley.labs.skratch

data class BDD constructor(val GIVEN: String, val WHEN: String, val THEN: String) {
    companion object {
        val So = So()
    }

    class So {
        infix fun GIVEN(GIVEN: String): Given = Given(GIVEN)
        data class Given(private val GIVEN: String) {
            infix fun WHEN(WHEN: String): When = When(GIVEN, WHEN)
            data class When(private val GIVEN: String, private val WHEN: String) {
                infix fun THEN(THEN: String): BDD = BDD(GIVEN, WHEN, THEN)
            }
        }
    }
}

fun main(args: Array<String>) {
    println(BDD.So GIVEN "an apple" WHEN "it falls" THEN "Newton thinks")
}
