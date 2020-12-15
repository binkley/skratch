package hm.binkley.labs.skratch.bdd.funcs

data class Qed(
    private val SCENARIO: String? = null,
    private val GIVEN: String? = null,
    private val WHEN: String? = null,
    private val THEN: String = caller(),
) {
    override fun toString() =
        "SCENARIO $SCENARIO: GIVEN $GIVEN WHEN $WHEN THEN $THEN"

    companion object {
        val SCENARIO = Scenario
        val GIVEN = Given()
        val WHEN = When()
        val THEN = Then()
        val QED = Qed()

        /** Inline to preserve the stack trace. */
        @Suppress("NOTHING_TO_INLINE")
        private inline fun caller(): String =
            Throwable().stackTrace[1].methodName
    }

    object Scenario {
        inline fun act(action: () -> Unit) =
            action().run { Given() }
    }

    data class Given(
        val SCENARIO: String = caller(),
    ) {
        inline fun act(action: () -> Unit) =
            action().run { When(SCENARIO) }
    }

    data class When(
        val SCENARIO: String? = null,
        val GIVEN: String = caller(),
    ) {
        inline fun act(action: () -> Unit) =
            action().run { Then(SCENARIO, GIVEN) }
    }

    data class Then(
        val SCENARIO: String? = null,
        val GIVEN: String? = null,
        val WHEN: String = caller(),
    ) {
        inline fun act(action: () -> Unit) =
            action().run { Qed(SCENARIO, GIVEN, WHEN) }
    }
}
