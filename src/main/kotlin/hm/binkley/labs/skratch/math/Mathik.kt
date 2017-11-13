package hm.binkley.labs.skratch.math

import java.lang.Math as nativeMath

inline infix fun Int.pow(that: Int) = nativeMath.pow(this.toDouble(),
        that.toDouble()).toInt()

fun main(args: Array<String>) {
    println(3 pow 4)

    println()

    for (n in -6..6) {
        val fib = Fib(n)
        println("f($n) = ${fib.char()}, F($n) = $fib, 1/F($n) = ${fib.toMat2x2().inv()}, |F($n)| = ${fib.det()}")
    }

    println()

    val a = (-6..6).map(::Fib).map(Fib::char)
    val b = (-6..6).map(::Fib).map { -it.char() * it.det() }.reversed()

    println(a)
    println(b)

    println()

    val p = (-6..6).map(::Fib)
    val q = (-6..6).map(::Fib).map { it.toMat2x2().inv() }.reversed()

    println(q)
    println(q)
    println("FIX ME - p == q ? ${p == q}")

    println()

    val fib1 = Mat2x2(0, 1, 1, 1)
    println("F(1)^-1 = ${fib1.inv()}")
    println("F(1)^-1 * F(1) = ${fib1.inv() * fib1}")
    println("F(1) * F(1)^-1 = ${fib1 * fib1.inv()}")

    println()

    for (n in -1..3)
        println("F(1)^$n = ${Fib.pow(n)}")
}
