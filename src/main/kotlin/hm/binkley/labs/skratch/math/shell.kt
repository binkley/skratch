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

fun main(args: Array<String>) {
    val cli = ArgParser("math.shell")
    val debug by cli.option(
        ArgType.Boolean,
        shortName = "d",
        fullName = "debug",
        description = "Enable debug output",
    ).default(false)
    cli.parse(args)

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
                answer = parse(line, debug).evaluate()
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

private fun parse(line: String, debug: Boolean = false): Expression {
    val parts = line.split('|',
        ignoreCase = false, limit = 2)
    val vars = parseVars(parts)

    val builder = ExpressionBuilder(parts[0])
    builder.variables(vars.keys)
    val expression = builder.build()
    expression.setVariables(vars)

    if (debug) err.println(expression)

    return expression
}

private fun parseVars(parts: List<String>) =
    if (1 == parts.size) mapOf()
    else comma.split(parts[1]).map {
        equal.split(it)
    }.map {
        it[0].trim() to parseValue(it[1])
    }.toMap()

private fun parseValue(expr: String) =
    ExpressionBuilder(expr).build().evaluate()
