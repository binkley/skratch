package hm.binkley.labs.skratch.math

import hm.binkley.labs.skratch.RichCLI
import hm.binkley.labs.skratch.isTty
import net.objecthunter.exp4j.ExpressionBuilder
import org.jline.reader.EndOfFileException
import org.jline.reader.UserInterruptException
import picocli.CommandLine.Command
import picocli.CommandLine.Parameters
import kotlin.system.exitProcess

private const val name = "math.shell"

@Command(
    description = ["Math shell"],
    mixinStandardHelpOptions = true,
    name = name,
    version = ["0-SNAPSHOT"],
)
class Options : Runnable {
    override fun run() {}

    @Parameters(
        description = ["Optional expression to evaluate."],
        paramLabel = "EXPRESSION",
    )
    var expression = arrayOf<String>()
}

fun main(args: Array<String>) {
    val cli = RichCLI(name, Options())
    cli.parse(args)

    val cliExpression = cli.options.expression.joinToString(" ")
    if (cliExpression.isNotEmpty()) {
        println(evaluateExpression(cliExpression, mapOf()).pretty)
        exitProcess(0)
    }

    val existingVars = mutableMapOf<String, Double>()

    if (!cli.terminal.isTty()) {
        generateSequence(::readLine).forEach {
            val answer = evaluateExpression(it, existingVars)
            println(answer.pretty)
            existingVars["_"] = answer.value
            existingVars += answer.newVars
        }

        return
    }

    cli.use {
        while (true) try {
            val line = it.readLine("> ").trim()
            if ("" == line) continue

            try {
                val answer = evaluateExpression(line, existingVars)
                it.println("@|bold %s|@", answer.pretty)
                existingVars["_"] = answer.value
                existingVars += answer.newVars
            } catch (e: Exception) {
                it.err.println("@|bold,red %s|@: %s", line, e.message)
                continue
            }
        } catch (e: EndOfFileException) {
            return
        } catch (e: UserInterruptException) {
            exitProcess(128 + 2) // Linux SIGINT
        }
    }
}

private val comma = Regex(" *, *")
private val equal = Regex(" *= *")

private data class Answer(
    val value: Double,
    val pretty: String,
    val newVars: Map<String, Double>,
)

private fun evaluateExpression(
    line: String,
    existingVars: Map<String, Double>,
): Answer {
    val parts = line.split('|', ignoreCase = false, limit = 2)

    val newVars: Map<String, Double>
    val builder: ExpressionBuilder
    when (parts.size) {
        1 -> {
            newVars = mapOf()
            builder = ExpressionBuilder(parts[0])
        }
        else -> {
            newVars = parseVars(parts[0])
            builder = ExpressionBuilder(parts[1])
        }
    }

    val vars = existingVars + newVars
    builder.variables(vars.keys)
    val expr = builder.build()
    expr.setVariables(vars)

    val answer = expr.evaluate()
    val pretty = answer.toBigDecimal().stripTrailingZeros().toString()

    return Answer(answer, pretty, newVars)
}

private fun parseVars(parts: String): Map<String, Double> {
    val vars = comma.split(parts).map {
        equal.split(it)
    }.map {
        val variable = it[0].trim()
        if ("_" == variable) error("Variable `_` is reserved")
        variable to evaluateAssignedValue(it[1])
    }.toMap()

    return vars
}

private fun evaluateAssignedValue(expr: String) =
    ExpressionBuilder(expr).build().evaluate()
