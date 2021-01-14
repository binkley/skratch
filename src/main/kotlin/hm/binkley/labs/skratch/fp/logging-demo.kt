package hm.binkley.labs.skratch.fp

import org.fusesource.jansi.AnsiConsole
import picocli.CommandLine.Help.Ansi.AUTO

interface BusinessLogger {
    fun debug(message: String, vararg args: Any?)
    fun info(message: String, vararg args: Any?)
    fun error(message: String, vararg args: Any?)
    fun businessInfo(message: String, vararg args: Any?)
    fun businessFailure(message: String, vararg args: Any?)
}

object ConsoleLogger : BusinessLogger {
    init {
        AnsiConsole.systemInstall()
    }

    override fun debug(message: String, vararg args: Any?) =
        println(AUTO.string("""
            @|faint DEBUG: ${message.format(args)}|@
        """.trimIndent()))

    override fun info(message: String, vararg args: Any?) =
        println(AUTO.string("""
            INFO: ${message.format(args)}
        """.trimIndent()))

    override fun error(message: String, vararg args: Any?) =
        println(AUTO.string("""
            @|bold,magenta ERROR: ${message.format(args)}|@
        """.trimIndent()))

    override fun businessInfo(message: String, vararg args: Any?) =
        println(AUTO.string("""
            [AUDIT] INFO: ${message.format(args)}
        """.trimIndent()))

    override fun businessFailure(message: String, vararg args: Any?) =
        println(AUTO.string("""
            @|bold,red [AUDIT] ERROR: ${message.format(args)}|@
        """.trimIndent()))
}

class BusinessException(reason: String) : Exception(reason)

fun succeed(thing: String): String = thing
fun fail(thing: String): String =
    throw BusinessException("This is not the right thing: $thing")

fun error(thing: String): String =
    throw IndexOutOfBoundsException("Write the code for $thing")

fun main() {
    val thing = "Veggie burger"
    val log = ConsoleLogger

    println(log.handle<BusinessException, String>("Thing", "HOKEY!") {
        succeed(thing)
    })
    println(log.handle<BusinessException, String>("Thing", "HOKEY!") {
        fail(thing)
    })
    println(log.handle<BusinessException, String>("Thing", "HOKEY!") {
        error(thing)
    })
}

inline fun <reified E : Exception, R : Any> BusinessLogger.handle(
    label: String,
    default: R,
    block: () -> R,
): R = try {
    debug("Trying $label")
    block().also {
        businessInfo("SUCCESS: $label")
    }
} catch (e: Exception) {
    if (e !is E) {
        error("ERROR: $label: $e")
        throw e
    }
    businessFailure("FAILURE: $label: $e")
    default
}
