package hm.binkley.labs.skratch.bdd.funcs

data class Qed(
    private val SCENARIO: String,
    private val GIVEN: String,
    private val WHEN: String,
    private val THEN: String = caller(),
) {
    override fun toString() =
        "SCENARIO $SCENARIO: GIVEN $GIVEN WHEN $WHEN THEN $THEN"

    companion object {
        val SCENARIO = Scenario
        val GIVEN = Given()
        val WHEN = When("-")
        val THEN = Then("-", "-")
        val QED = Qed("-", "-", "-")

        /**
         * Inline to preserve the stack trace.
         *
         * @todo Can this come from a stdlib/reflection feature?
         */
        @Suppress("NOTHING_TO_INLINE")
        private inline fun caller(): String =
            Throwable().stackTrace[1].methodName
    }

    object Scenario {
        inline fun act(action: () -> Unit): Given {
            action.invoke()
            return Given()
        }
    }

    data class Given(
        val SCENARIO: String = caller(),
    ) {
        inline fun act(action: () -> Unit): When {
            action.invoke()
            return When(SCENARIO)
        }
    }

    data class When(
        val SCENARIO: String,
        val GIVEN: String = caller(),
    ) {
        inline fun act(action: () -> Unit): Then {
            action.invoke()
            return Then(SCENARIO, GIVEN)
        }
    }

    data class Then(
        val SCENARIO: String,
        val GIVEN: String,
        val WHEN: String = caller(),
    ) {
        inline fun act(action: () -> Unit): Qed {
            action.invoke()
            return Qed(SCENARIO, GIVEN, WHEN)
        }
    }
}
