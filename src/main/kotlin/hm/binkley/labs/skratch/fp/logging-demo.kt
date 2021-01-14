package hm.binkley.labs.skratch.fp

import org.fusesource.jansi.AnsiConsole
import picocli.CommandLine.Help.Ansi.AUTO

interface BusinessLogger {
    fun debug(message: String, vararg args: Any?)
    fun businessInfo(message: String, vararg args: Any?)
    fun businessError(message: String, vararg args: Any?)
}

object ConsoleLogger : BusinessLogger {
    init {
        AnsiConsole.systemInstall()
    }

    override fun debug(message: String, vararg args: Any?) {
        println(AUTO.string("""
            @|faint DEBUG: ${message.format(args)}|@
        """.trimIndent()))
    }

    override fun businessInfo(message: String, vararg args: Any?) {
        println(AUTO.string("""
            [AUDIT] INFO: ${message.format(args)}
        """.trimIndent()))
    }

    override fun businessError(message: String, vararg args: Any?) {
        println(AUTO.string("""
            @|bold,red [AUDIT] ERROR: ${message.format(args)}|@
        """.trimIndent()))
    }
}

class BusinessException(reason: String) : Exception(reason)

fun <T> check(thing: T): T =
    throw BusinessException("This is not the right thing: $thing")

fun main() {
    val thing = "Veggie burger"
    val log = ConsoleLogger

    withLogging<BusinessException, Unit>(log, "Thing") {
        check(thing)
    }.also {
        println(it)
    }
}

inline fun <reified E : Exception, R : Any?> withLogging(
    log: BusinessLogger,
    label: String,
    block: () -> R,
): R {
    log.debug("Trying $label")
    try {
        val r = block()
        log.businessInfo("SUCCESS: $label")
        return r
    } catch (e: Exception) {
        val type = if (e is E) "FAILURE" else "ERROR"
        log.businessError("$type: $label: $e")
        throw e
    }
}
