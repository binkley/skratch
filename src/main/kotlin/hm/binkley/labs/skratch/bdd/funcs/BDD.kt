package hm.binkley.labs.skratch.bdd.funcs

import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.GIVEN
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.QED
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.SCENARIO
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.THEN
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.WHEN
import org.fusesource.jansi.AnsiConsole
import picocli.CommandLine.Help.Ansi.AUTO

fun main() {
    val scenario = SCENARIO `A revolution begins`
            GIVEN `an apple`
            WHEN `it falls`
            THEN `Newton thinks`
            QED
    println(scenario)

    // Expected to raise an exception
    SCENARIO `A revolution is missed`
            GIVEN `an apple`
            WHEN `it falls`
            THEN `Newton sleeps`
            QED
}

private const val CLAUSE_NAME_BUG = "<BUG: Clause name misassigned>"

fun String.asAnsi(vararg args: Any?): String = AUTO.string(format(args))

data class QED(
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
            // Throw an assertion restating the BDD failure spot, but do not
            // lose any of the original assertion failure info
            val x = AssertionError("Failed $label clause in:\n$this\n$e")
            x.stackTrace = e.stackTrace.filter {
                !it.className.startsWith(QED::class.qualifiedName!!)
            }.toTypedArray()
            e.suppressed.forEach { x.addSuppressed(it) }
            throw x
        }
    }

    override fun toString(): String = """
        @|underline $SCENARIO|@
            @|italic $GIVEN|@
            $WHEN
            @|bold $THEN|@
        """.trimIndent().asAnsi()

    companion object {
        init {
            AnsiConsole.systemInstall()
        }

        // Dummy values to provide nice syntax in test writing
        val SCENARIO = Scenario()
        val GIVEN = Given(SCENARIO)
        val WHEN = When(SCENARIO, GIVEN)
        val THEN = Then(SCENARIO, GIVEN, WHEN)
        val QED = QED(SCENARIO, GIVEN, WHEN, THEN)

        /** Inline to preserve the stack trace. */
        @Suppress("NOTHING_TO_INLINE")
        private inline fun caller() =
            Throwable().stackTrace[2].methodName
    }

    data class Scenario(
        internal var text: String = CLAUSE_NAME_BUG,
        private var action: () -> Unit = {},
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
        internal var text: String = CLAUSE_NAME_BUG,
        private val previousText: String = caller(),
        private var action: () -> Unit = {},
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
        internal var text: String = CLAUSE_NAME_BUG,
        private val previousText: String = caller(),
        private var action: () -> Unit = {},
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
        internal var text: String = CLAUSE_NAME_BUG,
        private val previousText: String = caller(),
        private var action: () -> Unit = {},
    ) : () -> Unit {
        init {
            WHEN.text = previousText
        }

        fun act(action: () -> Unit) = run {
            this.action = action
            QED(SCENARIO, GIVEN, WHEN, this)
        }

        override fun invoke() = action()
        override fun toString() = "THEN $text"
    }
}
