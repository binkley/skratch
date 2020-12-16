package hm.binkley.labs.skratch.bdd.funcs

data class Qed(
    private val SCENARIO: Scenario,
    private val GIVEN: Given,
    private val WHEN: When,
    private val THEN: Then,
    private val previousText: String = caller(),
) {
    init {
        THEN.text = previousText

        execute("GIVEN", GIVEN)
        execute("WHEN", WHEN)
        execute("THEN", THEN)
    }

    private inline fun execute(label: String, action: () -> Unit) {
        try {
            action()
        } catch (e: AssertionError) {
            val x = AssertionError("Failed $label in $this: $e")
            x.stackTrace = e.stackTrace
            throw x
        }
    }

    override fun toString() = "$SCENARIO: $GIVEN $WHEN $THEN"

    companion object {
        // Dummy values to provide nice syntax in test writing
        val SCENARIO = Scenario()
        val GIVEN = Given(SCENARIO)
        val WHEN = When(SCENARIO, GIVEN)
        val THEN = Then(SCENARIO, GIVEN, WHEN)
        val QED = Qed(SCENARIO, GIVEN, WHEN, THEN)

        /** Inline to preserve the stack trace. */
        @Suppress("NOTHING_TO_INLINE")
        private inline fun caller(): String =
            Throwable().stackTrace[2].methodName
    }

    data class Scenario(
        private var action: () -> Unit = {},
        internal var text: String? = null,
    ) : () -> Unit {
        fun act(action: () -> Unit) = run {
            this.action = action
            Given(this)
        }

        override fun invoke() = action()
        override fun toString() = "SCENARIO $text"
    }

    data class Given(
        val SCENARIO: Scenario,
        private var action: () -> Unit = {},
        internal var text: String? = null,
        private val previousText: String = caller(),
    ) : () -> Unit {
        init {
            SCENARIO.text = previousText
        }

        fun act(action: () -> Unit) = run {
            this.action = action
            When(SCENARIO, this)
        }

        override fun invoke() = action()
        override fun toString() = "GIVEN $text"
    }

    data class When(
        val SCENARIO: Scenario,
        val GIVEN: Given,
        private var action: () -> Unit = {},
        internal var text: String? = null,
        private val previousText: String = caller(),
    ) : () -> Unit {
        init {
            GIVEN.text = previousText
        }

        fun act(action: () -> Unit) = run {
            this.action = action
            Then(SCENARIO, GIVEN, this)
        }

        override fun invoke() = action()
        override fun toString() = "WHEN $text"
    }

    data class Then(
        val SCENARIO: Scenario,
        val GIVEN: Given,
        val WHEN: When,
        private var action: () -> Unit = {},
        internal var text: String? = null,
        private val previousText: String = caller(),
    ) : () -> Unit {
        init {
            WHEN.text = previousText
        }

        fun act(action: () -> Unit) = run {
            this.action = action
            Qed(SCENARIO, GIVEN, WHEN, this)
        }

        override fun invoke() = action()
        override fun toString() = "THEN $text"
    }
}
