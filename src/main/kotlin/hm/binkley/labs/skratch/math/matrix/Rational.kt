package hm.binkley.labs.skratch.math.matrix

import java.util.Objects
import kotlin.math.abs
import kotlin.math.sign

// TODO: Reuse FixedBigRational from kotlin-rational project
class Rational(n: Long, d: Long) :
    GeneralNumber<Rational, Rational>,
    Comparable<Rational> {
    val numerator: Long
    val denominator: Long

    init {
        if (0L == d) throw ArithmeticException("Denominator is zero")

        val (a, b) = normalizeSign(n, d)
        val gcd = gcd(abs(a), b)
        this.numerator = a / gcd
        this.denominator = b / gcd
    }

    constructor(n: Long) : this(n, 1L)

    override fun unaryMinus() = Rational(-numerator, denominator)
    override fun plus(other: Rational) =
        Rational(
            numerator * other.denominator + other.numerator * denominator,
            denominator * other.denominator
        )

    override fun unaryDiv() = Rational(denominator, numerator)
    override fun times(other: Rational) =
        Rational(numerator * other.numerator, denominator * other.denominator)

    override fun times(other: Long) = this * Rational(other)
    override fun div(other: Rational) = this * other.multInv
    override fun div(other: Long) = this / Rational(other)

    override val conj: Rational get() = this
    override val absoluteValue: Rational
        get() = Rational(abs(numerator), denominator)
    override val squareNorm: Rational get() = this
    val root: GeneralNumber<*, Rational> get() = root(this)

    override fun isZero() = this == ZERO
    val isPositive get() = this > ZERO
    val isNegative get() = this < ZERO
    override fun isUnit() = this == ONE
    val isWhole get() = 1L == denominator

    override fun compareTo(other: Rational) =
        (numerator * other.denominator)
            .compareTo(other.numerator * denominator)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Rational) return false

        return numerator == other.numerator && denominator == other.denominator
    }

    override fun equivalent(other: GeneralNumber<*, *>) = when (other) {
        is Rational -> numerator == other.numerator && denominator == other.denominator
        is Complex -> ZERO == other.im && numerator == other.re.numerator && denominator == other.re.denominator
        else -> TODO("BUG: This is a terrible approach")
    }

    override fun hashCode() = Objects.hash(numerator, denominator)

    override fun toString() =
        if (isWhole) "$numerator" else "$numerator/$denominator"

    companion object {
        private fun normalizeSign(
            a: Long,
            b: Long
        ) = if (-1 == b.sign) -a to -b else a to b

        private tailrec fun gcd(
            a: Long,
            b: Long
        ): Long = if (b == 0L) a else gcd(b, a % b)

        private fun root(c: Rational): GeneralNumber<*, Rational> {
            // TODO: WRONG -- does not work for Quarternions
            if (c.isNegative) {
                return Complex(
                    0L,
                    c.absoluteValue.root as Rational
                )
            }
            val (rn, nexact) = maybeExactRoot(c.numerator)
            val (rd, dexact) = maybeExactRoot(c.denominator)
            return when {
                nexact && dexact -> Rational(rn, rd)
                else -> newtonApproximation(c)
            }
        }

        private tailrec fun guessRoot(
            x: Long,
            start: Long,
            end: Long,
            guess: Long
        ): Long {
            if (start > end) return guess
            val mid = (start + end) / 2
            val mid2 = mid * mid
            return when {
                mid2 == x -> mid
                mid2 < x -> guessRoot(x, mid + 1, end, mid)
                else -> guessRoot(x, start, mid - 1, guess)
            }
        }

        private fun maybeExactRoot(x: Long) = when (x) {
            0L, 1L -> x to true
            else -> {
                val guess = guessRoot(x, 1L, x, 0L)
                guess to (x == guess * guess)
            }
        }

        private val EPSILON = Rational(1L, 1_000_000L)
        private fun newtonApproximation(c: Rational): Rational {
            var t = c
            while ((t - c / t).absoluteValue > EPSILON * t)
                t = (c / t + t) / TWO
            return t
        }

        val ZERO = Rational(0L)
        val ONE = Rational(1L)
        val TWO = Rational(2L)
    }
}

val Long.sign: Int // TODO: This is in Kotlin 1.2 - why not found on Mac?
    get() = when {
        this < 0L -> -1
        this == 0L -> 0
        else -> 1
    }

operator fun Long.plus(other: Rational) = Rational(this) + other
operator fun Long.minus(other: Rational) = Rational(this) - other
operator fun Long.times(other: Rational) = Rational(this) * other
operator fun Long.div(other: Rational) = Rational(this) / other
