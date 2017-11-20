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

    val fib1 = AnyMatrix2x2(0, 1, 1, 1)
    println("F(1)^-1 = ${fib1.inv}")
    println("F(1)^-1 * F(1) = ${fib1.inv * fib1}")
    println("F(1) * F(1)^-1 = ${fib1 * fib1.inv}")

    println()

    for (n in -3..3)
        println("F(1)^$n = ${Fib.pow(n)}")

    println((-8..8).map { Fib(it) }.map { it.trace }.toList())

    println()

    val fib8 = Fib(4)
    for (n in 3 downTo 0)
        println("F(4) / F($n) = ${fib8 / Fib(n)}")

    println()
}
