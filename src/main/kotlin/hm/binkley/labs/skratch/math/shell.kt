package hm.binkley.labs.skratch.math

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReaderBuilder
import org.jline.terminal.TerminalBuilder
import java.lang.System.err

private var DEBUG = false

fun main(args: Array<String>) {
    val cli = ArgParser("math.shell")
    val debug by cli.option(
        ArgType.Boolean,
        shortName = "d",
        fullName = "debug",
        description = "Enable debug output",
    ).default(false)
    cli.parse(args)

    DEBUG = debug

    AnsiConsole.systemInstall()

    TerminalBuilder.terminal().use { terminal ->
        val reader = LineReaderBuilder.builder()
            .terminal(terminal)
            .build()
        val writer = terminal.writer()

        while (true) try {
            val line = reader.readLine("> ")
            val answer: Double
            try {
                answer = parseExpression(line).evaluate()
            } catch (e: Exception) {
                err.println(ansi()
                    .bold().fgRed()
                    .format("%s", line)
                    .reset()
                    .format(": %s", e.message))
                continue
            }
            writer.println(ansi()
                .bold()
                .format("%s", answer)
                .reset())
        } catch (e: EndOfFileException) {
            return
        }
    }
}

private val comma = Regex(" *, *")
private val equal = Regex(" *= *")

private fun parseExpression(line: String): Expression {
    val parts = line.split('|', ignoreCase = false, limit = 2)

    if (DEBUG) println("parts: $parts")

    val vars: Map<String, Double>
    val builder: ExpressionBuilder
    when (parts.size) {
        1 -> {
            vars = mapOf()
            builder = ExpressionBuilder(parts[0])
        }
        else -> {
            vars = parseVars(parts[0])
            builder = ExpressionBuilder(parts[1])
        }
    }
    builder.variables(vars.keys)
    val expr = builder.build()
    expr.setVariables(vars)

    return expr
}

private fun parseVars(parts: String): Map<String, Double> {
    val vars = comma.split(parts).map {
        equal.split(it)
    }.map {
        it[0].trim() to evaluateAssignedValue(it[1])
    }.toMap()

    if (DEBUG) println("vars: $vars")

    return vars
}

private fun evaluateAssignedValue(expr: String): Double {
    if (DEBUG) println("expr: $expr")

    return ExpressionBuilder(expr).build().evaluate()
}
