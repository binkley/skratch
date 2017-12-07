package hm.binkley.labs.skratch.math.matrix

import java.util.Objects

/**
 * @see https://introcs.cs.princeton.edu/java/92symbolic/BigRational.java.html
 */
class Rational(n: Long, d: Long) : Number<Rational>, Comparable<Rational> {
    val n: Long
    val d: Long

    init {
        if (0L == d) throw ArithmeticException("Denominator is zero")

        val (za, zb) = zero(n, d)
        val (sa, sb) = sign(za, zb)
        val gcd = gcd(sa, sb)
        this.n = sa / gcd
        this.d = sb / gcd
    }

    constructor(n: Long) : this(n, 1)

    override fun unaryMinus() = Rational(-n, d)
    override fun plus(that: Rational)
            = Rational(n * that.d + that.n * d, d * that.d)

    override fun minus(that: Rational) = this + -that
    override fun times(that: Rational) = Rational(n * that.n, d * that.d)
    override fun times(that: Long) = this * Rational(that)
    override fun div(that: Rational) = this * that.inv
    override fun div(that: Long) = this / Rational(that)

    override val inv: Rational
        get() = Rational(d, n)
    override val conj: Rational
        get() = this
    override val abs: Rational
        get() = Rational(Math.abs(n), d)
    override val sqnorm: Rational
        get() = this * this

    override fun isZero() = 0L == n
    override fun isUnit() = 1L == n && 1L == d

    override fun compareTo(other: Rational)
            = (n * other.d).compareTo(other.n * d)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Rational) return false

        return n == other.n && d == other.d
    }

    override fun hashCode() = Objects.hash(n, d)

    override fun toString() = "$n/$d"

    companion object {
        private fun sign(a: Long, b: Long)
                = if (b < 0) -a to -b else a to b

        private fun zero(a: Long, b: Long)
                = if (0L == a) 0L to 1L else a to b

        private tailrec fun gcd(a: Long, b: Long): Long
                = if (b == 0L) a else gcd(b, a % b)

        val ZERO = Rational(0L)
        val ONE = Rational(1L)
        val TWO = Rational(2L)
    }
}
