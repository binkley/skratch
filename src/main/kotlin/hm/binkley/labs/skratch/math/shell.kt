package hm.binkley.labs.skratch.math

import hm.binkley.labs.skratch.ColorfulCli
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder
import org.jline.reader.EndOfFileException
import org.jline.reader.UserInterruptException
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import picocli.CommandLine.Parameters
import kotlin.system.exitProcess

private const val name = "math.shell"

private var DEBUG = false

@Command(
    description = ["Math shell"],
    mixinStandardHelpOptions = true,
    name = name,
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
    val cli = ColorfulCli(name, Options())
    cli.parse(args)

    DEBUG = cli.options.debug

    val cliExpression = cli.options.expression.joinToString(" ")
    if (cliExpression.isNotEmpty()) {
        println(parseExpression(cliExpression).evaluate())
        exitProcess(0)
    }

    cli.use {
        while (true) try {
            val line = it.readLine("> ")
            val answer: Double
            try {
                answer = parseExpression(line).evaluate()
            } catch (e: Exception) {
                it.err.println("@|bold,red %s|@: %s", line, e.message)
                continue
            }
            it.println("@|bold %s|@", answer)
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
