package hm.binkley.labs.skratch.bdd.funcs

data class Qed(
    private val GIVEN: String,
    private val WHEN: String,
    private val THEN: String = caller(),
) {
    override fun toString() = "GIVEN $GIVEN WHEN $WHEN THEN $THEN"

    companion object {
        val GIVEN = Given
        val WHEN = When()
        val THEN = Then("-")
        val QED = Qed("-", "-")

        // `caller` is inline to preserve the stack trace
        @Suppress("NOTHING_TO_INLINE")
        private inline fun caller(): String =
            Throwable().stackTrace[1].methodName
    }

    object Given {
        inline fun act(action: () -> Unit): When {
            action.invoke()
            return When()
        }
    }

    data class When(val GIVEN: String = caller()) {
        inline fun act(action: () -> Unit): Then {
            action.invoke()
            return Then(GIVEN)
        }
    }

    data class Then(val GIVEN: String, val WHEN: String = caller()) {
        inline fun act(action: () -> Unit): Qed {
            action.invoke()
            return Qed(GIVEN, WHEN)
        }
    }
}
