package hm.binkley.labs.skratch.math.matrix

import org.fusesource.jansi.AnsiConsole
import picocli.CommandLine.Help.Ansi.AUTO

fun main() {
    AnsiConsole.systemInstall()

    println(FibMatrix(0) pow 4)
    println(FibMatrix(2) pow 4)

    for (n in -6..6) {
        val fib = FibMatrix(n)
        println()
        println(
            AUTO.string(
                """
@|bold Characteristic ${fib.characteristic}|@
@|green F($n) = $fib|@
@|blue 1/F($n) = ${fib.multInv}|@
@|magenta det(F($n)) = ${fib.det}|@
@|yellow tr(F($n)) = ${fib.tr}|@
                """.trim()
            )
        )
    }

    println()

    val a =
        (-6..6)
            .map {
                FibMatrix(it)
            }.map {
                it.characteristic
            }
    val b =
        (-6..6)
            .map {
                FibMatrix(it)
            }.map {
                -it.characteristic * it.det
            }.reversed()

    println(a)
    println(b)

    println()

    val p = (-6L..6).map { FibMatrix(it) }
    val q =
        (-6..6)
            .map { FibMatrix(it) }
            .map { it.multInv }
            .reversed()

    println(p)
    println(q)
    println("p == rev(1/p) is ${p == q}")

    println()

    val fib4 = FibMatrix(4)
    for (n in 3 downTo 0) {
        println("F(4) / F($n) = ${fib4 / FibMatrix(n)}")
    }

    println()

    fun superscript(n: Int): String =
        when (n) {
            0 -> "\u2070"
            1 -> "\u00B9"
            2 -> "\u00B2"
            3 -> "\u00B3"
            4 -> "\u2074"
            5 -> "\u2075"
            6 -> "\u2076"
            7 -> "\u2077"
            8 -> "\u2078"
            9 -> "\u2079"
            else -> "\u207B${superscript(-n)}"
        }

    val fib1 = FibMatrix(1)
    for (n in -3..3) {
        println("F(1)${superscript(n)} = ${fib1 pow n}")
    }

    val fib9 = FibMatrix(9)
    println("${superscript(9)}√F(9) = ${fib9 root 9}")
    println("${superscript(3)}√F(9) = ${fib9 root 3}")
    println("${superscript(-3)}√F(9) = ${fib9 root -3}")
    println("${superscript(-9)}√F(9) = ${fib9 root -9}")

    println()
    println("== CHARACTERISTIC VS DETERMINANT VS TRACE")

    (-8..8)
        .map {
            FibMatrix(it)
        }.map {
            "%2s | %2s | %3s".format(it.characteristic, it.det, it.tr)
        }.forEach {
            println(it)
        }

    println()
    println("== PAULI SPIN MATRICES")

    println(Pauli)

    println("PRODUCTS")
    Pauli.values.forEach { m ->
        Pauli.values
            .map { n ->
                val x = m * n
                val y = n * m
                val diff = x.toGeneric() - y.toGeneric()
                "${m.symbol} * ${n.symbol} -> ${(m * n).symbol}; ${m.symbol} / ${n.symbol} -> ${(m / n).symbol}; [${m.symbol}, ${n.symbol}] -> $diff"
            }.sorted()
            .forEach {
                println(it)
            }
    }
}
