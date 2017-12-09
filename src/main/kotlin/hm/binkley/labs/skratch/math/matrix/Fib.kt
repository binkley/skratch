package hm.binkley.labs.skratch.math.matrix

import hm.binkley.labs.skratch.math.matrix.Rational.Companion.ONE
import hm.binkley.labs.skratch.math.matrix.Rational.Companion.ZERO

class Fib(val char: Int) : Matrix2x2<Rational, Rational, Fib>(nthFib(char)) {
    override fun elementCtor(n: Long) = Rational(n)

    override fun matrixCtor(
            a: Rational, b: Rational, c: Rational, d: Rational)
            = throw AssertionError("BUG: Did not override other method")

    override val T: Fib
        get() = this
    override val conj: Fib
        get() = this
    override val adj: Fib
        get() = throw UnsupportedOperationException("No adj of a Fibonacci")

    override fun unaryMinus()
            = throw UnsupportedOperationException(
            "No negative of a Fibonacci")

    override fun plus(that: Fib)
            = TODO("Type system should forbid: Fibonaccis not additive")

    override fun minus(that: Fib)
            = TODO("Type system should forbid: Fibonaccis not additive")

    override fun times(that: Fib) = Fib(char + that.char)

    override fun times(other: Rational)
            = TODO("Type system should forbid: Fibonaccis are closed")

    override fun div(that: Fib) = Fib(char = that.char)

    override fun div(other: Rational)
            = TODO("Type system should forbid: Fibonaccis are closed")

    override fun div(other: Long)
            = TODO("Type system should forbid: Fibonaccis are closed")

    override fun toString() = "F($char)"

    companion object {
        private val fib0 = Holder(ONE, ZERO, ZERO, ONE)

        private fun nthFib(char: Int)
                = when {
            char >= 0 -> nextFib(char, fib0)
            else -> prevFib(char, fib0)
        }

        private tailrec fun nextFib(n: Int, m: Holder<Rational, Rational>)
                : Holder<Rational, Rational>
                = if (0 == n) m
        else nextFib(n - 1, Holder(m.b, m.d, m.d, m.b + m.d))

        private tailrec fun prevFib(n: Int, m: Holder<Rational, Rational>)
                : Holder<Rational, Rational>
                = if (0 == n) m
        else prevFib(n + 1, Holder(m.b - m.a, m.a, m.a, m.b))
    }
}
