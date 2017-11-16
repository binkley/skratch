package hm.binkley.labs.skratch.math.shell

import net.objecthunter.exp4j.ExpressionBuilder
import org.fusesource.jansi.Ansi
import org.fusesource.jansi.AnsiConsole
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReaderBuilder
import org.jline.terminal.TerminalBuilder
import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.Function
import java.io.PrintWriter

fun main(args: Array<String>) {
    AnsiConsole.systemInstall()
    val ansi = Ansi.ansi()

    val s = Function("""
s(n, x) = if( x >= 1, n, s(n+1, x + rUni(0,1) ) )
""")
    val e = Expression("avg( i, 1, 10000, s(0,0) )", s)
    println(e.calculate())

    println()

    TerminalBuilder.terminal().use { terminal ->
        val reader = LineReaderBuilder.builder().
                terminal(terminal).
                build()
        val writer = PrintWriter(terminal.writer())
        while (true) {
            try {
                val line = reader.readLine("> ")
                val answer = ExpressionBuilder(line).build().evaluate()

                if (answer.isNaN()) {
                    System.err.println(ansi.render("@|bold,red $line|@"))
                    continue
                }
                writer.println(ansi.render("@|bold $answer|@"))
            } catch (e: EndOfFileException) {
                return
            }
        }
    }
}
