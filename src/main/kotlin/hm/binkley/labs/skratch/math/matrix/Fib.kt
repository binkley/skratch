package hm.binkley.labs.skratch.math.matrix

import hm.binkley.labs.skratch.math.matrix.Rational.Companion.ONE
import hm.binkley.labs.skratch.math.matrix.Rational.Companion.ZERO

class Fib(
    val char: Long,
) : Matrix2x2<Rational, Rational, Fib>(nthFib(char)) {
    constructor(char: Int) : this(char.toLong())

    override fun elementCtor(n: Long) = Rational(n)

    override fun matrixCtor(
        a: Rational, b: Rational, c: Rational,
        d: Rational,
    ) = throw AssertionError(
        "BUG: Did not override other method")

    override val T: Fib get() = this
    override val multiplicativeInverse: Fib get() = Fib(-char)
    override val conj: Fib get() = this
    override val adj: Fib get() = TODO("No adj of a Fibonacci")

    override fun unaryMinus() = TODO("Think through")
    override fun plus(other: Fib) = TODO("Think through")
    override fun minus(other: Fib) = TODO("Think through")
    override fun times(other: Fib) = Fib(char + other.char)
    override fun times(other: Rational) = TODO("Think through")
    override fun div(other: Fib) = Fib(char - other.char)
    override fun div(other: Rational) = TODO("Think through")
    override fun div(other: Long) = TODO("Think through")

    infix fun pow(other: Long) = Fib(char * other)
    infix fun pow(other: Int) = pow(other.toLong())
    infix fun root(other: Long) = when {
        0L != char % other -> throw ArithmeticException()
        else -> Fib(char / other)
    }

    infix fun root(other: Int) = root(other.toLong())

    companion object {
        private val fib0 = Holder(ONE, ZERO, ZERO, ONE)

        private fun nthFib(char: Long) = when {
            char >= 0 -> nextFib(char, fib0)
            else -> prevFib(char, fib0)
        }

        private tailrec fun nextFib(n: Long, m: Holder<Rational, Rational>)
                : Holder<Rational, Rational> = if (0L == n) m
        else nextFib(n - 1, Holder(m.b, m.d, m.d, m.b + m.d))

        private tailrec fun prevFib(n: Long, m: Holder<Rational, Rational>)
                : Holder<Rational, Rational> = if (0L == n) m
        else prevFib(n + 1, Holder(m.b - m.a, m.a, m.a, m.b))
    }
}
