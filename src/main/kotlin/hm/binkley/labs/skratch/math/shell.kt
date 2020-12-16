package hm.binkley.labs.skratch.math

import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.terminal.TerminalBuilder
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters
import java.lang.System.err
import kotlin.system.exitProcess

private var DEBUG = false

@Command(
    description = ["Math shell"],
    mixinStandardHelpOptions = true,
    name = "math.shell",
    version = ["0-SNAPSHOT"],
)
class Options : Runnable {
    override fun run() {}

    @Option(
        description = ["Enable debug output."],
        names = ["-d", "--debug"],
    )
    var debug = false

    @Parameters(
        description = ["Optional expression to evaluate."],
        paramLabel = "EXPRESSION",
    )
    var expression = arrayOf<String>()
}

fun main(args: Array<String>) {
    val options = Options()
    val cli = CommandLine(options)
    cli.execute(*args)

    if (cli.isUsageHelpRequested || cli.isVersionHelpRequested) return

    DEBUG = options.debug

    val cliExpression = options.expression.joinToString(" ")

    if (cliExpression.isNotEmpty()) {
        println(parseExpression(cliExpression).evaluate())
        exitProcess(0)
    }

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
        } catch (e: UserInterruptException) {
            exitProcess(128 + 2) // Linux SIGINT
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
