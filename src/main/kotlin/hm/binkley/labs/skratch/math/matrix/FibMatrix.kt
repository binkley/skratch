package hm.binkley.labs.skratch.math.matrix

import hm.binkley.labs.skratch.math.matrix.Rational.Companion.ONE
import hm.binkley.labs.skratch.math.matrix.Rational.Companion.ZERO

class FibMatrix(
    val characteristic: Long,
) : Matrix2x2<Rational, Rational, FibMatrix>(nthFib(characteristic)) {
    constructor(char: Int) : this(char.toLong())

    override val det: Rational
        get() = if (0L == characteristic % 2) ONE else -ONE

    override fun elementCtor(n: Long) = Rational(n)

    override fun matrixCtor(
        a: Rational,
        b: Rational,
        c: Rational,
        d: Rational,
    ) = throw AssertionError("BUG: Did not override other method")

    override val T: FibMatrix get() = this
    override val conj: FibMatrix get() = this
    override val adj: FibMatrix get() = TODO("No adj of a Fibonacci")

    /** TODO: Consider `-Fib(n) ≡ Fib(-n)`? */
    override fun unaryMinus() = TODO("Think through")
    override fun plus(other: FibMatrix) = TODO("Think through")
    override fun minus(other: FibMatrix) = TODO("Think through")
    override fun times(other: FibMatrix) =
        FibMatrix(characteristic + other.characteristic)

    override fun unaryDiv() = FibMatrix(-characteristic)
    override fun times(other: Rational) = TODO("Think through")
    override fun div(other: FibMatrix) =
        FibMatrix(characteristic - other.characteristic)

    override fun div(other: Long) = TODO("Think through")

    // TODO: Use rationals, not integers; integers are a special case
    infix fun pow(other: Long) = FibMatrix(characteristic * other)
    infix fun pow(other: Int) = pow(other.toLong())
    infix fun root(other: Long) = when {
        0L != characteristic % other -> throw ArithmeticException()
        else -> FibMatrix(characteristic / other)
    }

    infix fun root(other: Int) = root(other.toLong())

    override fun toString() = "Fib($characteristic)${super.toString()}"

    companion object {
        private val fib0 = Holder(ONE, ZERO, ZERO, ONE)
        val generator = FibMatrix(1)

        private fun nthFib(char: Long) = when {
            char >= 0 -> nextFib(char, fib0)
            else -> prevFib(char, fib0)
        }

        private tailrec fun nextFib(n: Long, m: Holder<Rational, Rational>): Holder<Rational, Rational> =
            if (0L == n) m
            else nextFib(n - 1, Holder(m.b, m.d, m.d, m.b + m.d))

        private tailrec fun prevFib(n: Long, m: Holder<Rational, Rational>): Holder<Rational, Rational> =
            if (0L == n) m
            else prevFib(n + 1, Holder(m.b - m.a, m.a, m.a, m.b))
    }
}
