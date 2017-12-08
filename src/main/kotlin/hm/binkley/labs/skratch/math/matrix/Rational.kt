package hm.binkley.labs.skratch.math.matrix

import java.util.Objects
import kotlin.math.sign

/**
 * @see https://introcs.cs.princeton.edu/java/92symbolic/BigRational.java.html
 */
class Rational(n: Long, d: Long)
    : Number<Rational, Rational>, Comparable<Rational> {
    val n: Long
    val d: Long

    init {
        if (0L == d) throw ArithmeticException("Denominator is zero")

        val (a, b) = sign(zero(n, d))
        val gcd = gcd(Math.abs(a), b)
        this.n = a.sign * a / gcd
        this.d = b / gcd
    }

    constructor(n: Long) : this(n, 1L)

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
        get() = this
    val root: Rational
        get() = root(this)

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
        private fun zero(a: Long, b: Long)
                = if (0L == a) 0L to 1L else a to b

        private fun sign(terms: Pair<Long, Long>)
                = if (terms.second < 0L) -terms.first to -terms.second else terms

        private tailrec fun gcd(a: Long, b: Long): Long
                = if (b == 0L) a else gcd(b, a % b)

        private fun root(c: Rational): Rational {
            if (c < ZERO) TODO("Return complex root of negative")
            val (rn, nexact) = tryExactRoot(c.n)
            val (rd, dexact) = tryExactRoot(c.d)
            return when {
                nexact && dexact -> Rational(rn, rd)
                else -> newtonApproximation(c)
            }
        }

        private fun tryExactRoot(x: Long): Pair<Long, Boolean> {
            // If x is a perfect square
            when (x) {
                0L, 1L -> return x to true
                else -> {
                    var start = 1L
                    var end = x
                    var ans = 0L
                    while (start <= end) {
                        val mid = (start + end) / 2
                        when {
                            mid * mid == x -> return mid to true
                            mid * mid < x -> {
                                start = mid + 1
                                ans = mid
                            }
                            else -> end = mid - 1
                        }
                    }
                    if (x == ans * ans) throw AssertionError(
                            "Exact square root; expected approximate")
                    return ans to false
                }
            }
        }

        private val EPSILON = Rational(1L, 1_000_000L)
        private fun newtonApproximation(c: Rational): Rational {
            var t = c
            while ((t - c / t).abs > EPSILON * t)
                t = (c / t + t) / TWO
            return t
        }

        val ZERO = Rational(0L)
        val ONE = Rational(1L)
        val TWO = Rational(2L)
    }
}
