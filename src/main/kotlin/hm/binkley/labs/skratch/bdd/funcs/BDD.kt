package hm.binkley.labs.skratch.bdd.funcs

data class Qed(val GIVEN: String, val WHEN: String,
        val THEN: String = caller()) {
    override fun toString() = "GIVEN $GIVEN WHEN $WHEN THEN $THEN"

    companion object {
        val GIVEN = Given
        val WHEN = When()
        val THEN = Then("-")
        val QED = Qed("-", "-")

        // `caller` is inline to preserve the stack trace
        private inline fun caller() = Throwable().stackTrace[1].methodName!!
    }

    object Given {
        inline fun upon(action: () -> Unit): When {
            action.invoke()
            return When()
        }
    }

    data class When(val GIVEN: String = caller()) {
        inline fun upon(action: () -> Unit): Then {
            action.invoke()
            return Then(GIVEN)
        }
    }

    data class Then(val GIVEN: String, val WHEN: String = caller()) {
        inline fun upon(action: () -> Unit): Qed {
            action.invoke()
            return Qed(GIVEN, WHEN)
        }
    }
}
