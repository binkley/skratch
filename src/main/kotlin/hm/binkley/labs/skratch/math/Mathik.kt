package hm.binkley.labs.skratch.math

import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import java.lang.Math as nativeMath

fun main(args: Array<String>) {
    AnsiConsole.systemInstall()

    println(Fib(0) pow 4)
    println(Fib(2) pow 4)

    for (n in -6..6) {
        val fib = Fib(n)
        println()
        println(ansi().render("""
@|bold f($n) = ${fib.char}|@
@|green F($n) = $fib|@
@|blue 1/F($n) = ${fib.toMat2x2().inv}|@
@|magenta det(F($n)) = ${fib.det}|@
@|yellow Tr(F($n) = ${fib.trace}|@
""".trim()))
    }

    println()

    val a = (-6..6).map(::Fib).map(Fib::char)
    val b = (-6..6).map(::Fib).map { -it.char * it.det }.reversed()

    println(a)
    println(b)

    println()

    val p = (-6..6).map(::Fib).map { it.toMat2x2() }
    val q = (-6..6).map(::Fib).map { it.toMat2x2().inv }.reversed()

    println(p)
    println(q)
    println("p == rev(1/p) is ${p == q}")

    println()

    val mat1 = AnyMatrix2x2(0, 1, 1, 1)
    println("F(1)^-1 = ${mat1.inv}")
    println("F(1)^-1 * F(1) = ${mat1.inv * mat1}")
    println("F(1) * F(1)^-1 = ${mat1 * mat1.inv}")

    println()

    val fib4 = Fib(4)
    for (n in 3 downTo 0)
        println("F(4) / F($n) = ${fib4 / Fib(n)}")

    println()

    val fib1 = Fib(1)
    for (n in -3..3)
        println("F(1)^$n = ${fib1 pow n}")

    fun rootsum(n: Int): String = when (n) {
        0 -> "\u2070"
        1 -> "\u00B8"
        2 -> "\u00B2"
        3 -> "\u00B3"
        4 -> "\u2074"
        else -> "\u207B${rootsum(-n)}"
    }

    for (n in -4..4 step 2)
        println("${rootsum(n)}√F(4) = ${fib4 root n}")

    println((-8..8).map { Fib(it) }.map { it.trace }.toList())

    println()
}
