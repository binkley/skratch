package hm.binkley.labs.skratch.math

import org.ejml.data.Complex_F64
import org.fusesource.jansi.Ansi.ansi
import org.fusesource.jansi.AnsiConsole
import java.lang.Math as nativeMath

inline infix fun Int.pow(that: Int) = nativeMath.pow(this.toDouble(),
        that.toDouble()).toInt()

operator fun Complex_F64.times(that: Complex_F64) = this.times(that)!!

fun main(args: Array<String>) {
    AnsiConsole.systemInstall()
    val ansi = ansi()

    println(3 pow 4)

    for (n in -6..6) {
        val fib = Fib(n)
        println()
        println(ansi.render("""
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

    println()

    val c1 = Complex_F64(1.0, 2.0)
    val c2 = Complex_F64(2.0, 1.0)
    println(c1 * c2)
}
