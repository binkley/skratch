@file:Suppress("FunctionName")

package hm.binkley.labs.skratch.bdd.strings

data class BDD constructor(
    val SCENARIO: String,
    val GIVEN: String,
    val WHEN: String,
    val THEN: String,
) {
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

        fun upon(clause: String, action: () -> Unit) {
            actions[clause] = action
        }
    }

    class So {
        infix fun SCENARIO(SCENARIO: String) = Scenario(SCENARIO)
        data class Scenario(private val SCENARIO: String) {
            infix fun GIVEN(GIVEN: String) = Given(SCENARIO, GIVEN)
            data class Given(
                private val SCENARIO: String,
                private val GIVEN: String,
            ) {
                infix fun WHEN(WHEN: String) = When(SCENARIO, GIVEN, WHEN)
                data class When(
                    private val SCENARIO: String,
                    private val GIVEN: String,
                    private val WHEN: String,
                ) {
                    infix fun THEN(THEN: String) =
                        BDD(SCENARIO, GIVEN, WHEN, THEN)
                }
            }
        }
    }
}
