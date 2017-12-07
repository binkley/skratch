package hm.binkley.labs.skratch.math

import hm.binkley.labs.skratch.math.Fib.Companion.fib
import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import java.lang.Math as nativeMath

fun main(args: Array<String>) {
    AnsiConsole.systemInstall()

    println(fib(0) pow 4)
    println(fib(2) pow 4)

    for (n in -6..6) {
        val fib = fib(n)
        println()
        println(ansi().render("""
@|bold f($n) = ${fib.char}|@
@|green F($n) = $fib|@
@|blue 1/F($n) = ${AnyMatrix2x2(fib).inv}|@
@|magenta det(F($n)) = ${fib.det}|@
@|yellow Tr(F($n)) = ${fib.trace}|@
""".trim()))
    }

    println()

    val a = (-6..6).map { fib(it) }.map { it.char }
    val b = (-6..6).map { fib(it) }.map { -it.char * it.det }.reversed()

    println(a)
    println(b)

    println()

    val p = (-6..6).map { fib(it) }.map { AnyMatrix2x2(it) }
    val q = (-6..6).map { fib(it) }.map { AnyMatrix2x2(it).inv }.reversed()

    println(p)
    println(q)
    println("p == rev(1/p) is ${p == q}")

    println()

    val fib4 = fib(4)
    for (n in 3 downTo 0)
        println("F(4) / F($n) = ${fib4 / fib(n)}")

    println()

    fun superscript(n: Int): String = when (n) {
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

    val fib1 = fib(1)
    for (n in -3..3)
        println("F(1)${superscript(n)} = ${fib1 pow n}")

    val fib9 = fib(9)
    println("${superscript(9)}√F(9) = ${fib9 root 9}")
    println("${superscript(3)}√F(9) = ${fib9 root 3}")
    println("${superscript(-3)}√F(9) = ${fib9 root -3}")
    println("${superscript(-9)}√F(9) = ${fib9 root -9}")

    println()

    println((-8..8).map { fib(it) }.map { it.trace }.toList())
}
