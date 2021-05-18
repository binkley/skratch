package hm.binkley.labs.skratch.bdd.funcs

import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.GIVEN
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.QED
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.SCENARIO
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.THEN
import hm.binkley.labs.skratch.bdd.funcs.QED.Companion.WHEN
import org.fusesource.jansi.AnsiConsole
import picocli.CommandLine.Help.Ansi.AUTO
import java.lang.System.out
import kotlin.reflect.KParameter

fun main() {
    println("== HAPPY PATH PASSING")
    val scenario = SCENARIO `A revolution begins`
            GIVEN `an apple`
            WHEN `it falls`
            THEN `Newton thinks`
            QED
    println(scenario)

    println()
    println("== HAPPY PATH FAILS (BROKEN CODE)")
    try {
        SCENARIO `A snack helps with thinking`
                GIVEN `an apple`
                WHEN `it falls`
                THEN `Newton eats the apple`
                QED
    } catch (e: AssertionError) {
        e.printStackTrace(out)
    }

    println()
    println("== HAPPY PATH SET UP WRONGLY (BAD TEST)")
    // Expected to raise an exception
    try {
        SCENARIO `A revolution is missed`
                GIVEN `an apple`
                WHEN `it falls`
                THEN `Newton sleeps`
                QED
    } catch (e: AssertionError) {
        e.printStackTrace(out)
    }
}

private const val CLAUSE_NAME_BUG = "<BUG: Clause name misassigned>"

fun String.asAnsi(vararg args: Any?): String = AUTO.string(format(args))

sealed interface TestResult
object PASS : TestResult
data class FAIL(val reason: String) : TestResult
data class ERROR(val e: Exception) : TestResult

data class QED(
    private val SCENARIO: Scenario,
    private val GIVEN: Given,
    private val WHEN: When,
    private val THEN: Then,
    private val previousText: String = caller(),
    private var result: TestResult =
        ERROR(IllegalStateException("BUG: Not executed")),
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
            result = PASS
        } catch (e: AssertionError) {
            result = FAIL(e.message ?: "No reason to fail")

            // Throw an assertion restating the BDD failure spot, but do not
            // lose any of the original assertion failure info
            val x = AssertionError("Failed $label clause in:\n$this\n$e")
            x.stackTrace = e.stackTrace.filter {
                !it.className.startsWith(QED::class.qualifiedName!!)
            }.toTypedArray()
            e.suppressed.forEach { x.addSuppressed(it) }
            throw x
        } catch (e: Exception) {
            result = ERROR(e)

            // Throw an assertion restating the BDD failure spot, but do not
            // lose any of the original assertion failure info
            val x = e::class.constructors.filter {
                it.parameters.map { it.type.classifier } == listOf(String::class)
            }.map {
                it.call("Errored $label clause in:\n$this\n$e")
            }.firstOrNull()
                ?: throw IllegalStateException("BUG: Exception does not accept a reason")
            x.stackTrace = e.stackTrace.filter {
                !it.className.startsWith(QED::class.qualifiedName!!)
            }.toTypedArray()
            e.suppressed.forEach { x.addSuppressed(it) }
            throw x
        }
    }

    override fun toString(): String {
        val mark = when (result) {
            PASS -> "@|green ✓|@"
            is FAIL -> "@|red ✗|@"
            is ERROR -> "@|cyan ‽|@"
        }
        return """
            $mark @|underline $SCENARIO|@
                @|italic $GIVEN|@
                $WHEN
                @|bold $THEN|@
            """.trimIndent().asAnsi()
    }

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
