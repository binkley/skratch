package hm.binkley.labs.skratch.factories

import hm.binkley.labs.skratch.factories.BigRational.Companion.ONE
import hm.binkley.labs.skratch.factories.BigRational.Companion.valueOf
import java.math.BigInteger
import java.util.Objects.hash

val Int.big: BigInteger get() = toBigInteger()
val BigInteger.rat: BigRational get() = valueOf(this)
val Int.rat: BigRational get() = valueOf(big)

class BigRational private constructor(
    val numerator: BigInteger,
    val denominator: BigInteger,
) : Comparable<BigRational> {
    override fun compareTo(other: BigRational) =
        (numerator * other.denominator)
            .compareTo(other.numerator * denominator)

    override fun equals(other: Any?) = this === other ||
        other is BigRational &&
        numerator == other.numerator &&
        denominator == other.denominator

    override fun hashCode() = hash(numerator, denominator)
    override fun toString() =
        if (BigInteger.ONE == denominator) "$numerator"
        else "$numerator/$denominator"

    companion object {
        val ZERO = BigRational(0.big, 1.big)
        val ONE = BigRational(1.big, 1.big)

        fun valueOf(
            numerator: BigInteger,
            denominator: BigInteger = BigInteger.ONE,
        ): BigRational {
            if (BigInteger.ZERO == denominator)
                throw ArithmeticException("Division by zero")
            if (BigInteger.ZERO == numerator) return ZERO

            var n = numerator
            var d = denominator
            if (-1 == d.signum()) {
                n = n.negate()
                d = d.negate()
            }

            val gcd = n.gcd(d)
            if (BigInteger.ONE != gcd) {
                n /= gcd
                d /= gcd
            }

            if (BigInteger.ONE == d) when {
                BigInteger.ONE == n -> return ONE
            }

            return BigRational(n, d)
        }
    }
}

infix fun BigInteger.over(other: BigInteger) = valueOf(this, other)
infix fun Int.over(denominator: Int) = big over denominator.big

infix operator fun BigRational.plus(other: BigRational) = valueOf(
    numerator * other.denominator + other.numerator * denominator,
    denominator * other.denominator
)

infix operator fun BigRational.minus(other: BigRational) = valueOf(
    numerator * other.denominator - other.numerator * denominator,
    denominator * other.denominator
)

infix operator fun BigRational.times(other: BigRational) = valueOf(
    numerator * other.numerator,
    denominator * other.denominator
)

infix operator fun BigRational.div(other: BigRational) = valueOf(
    numerator * other.denominator,
    denominator * other.numerator
)

fun BigRational.truncate() = (numerator / denominator).rat

fun BigRational.divideAndRemainder(other: BigRational):
    Pair<BigRational, BigRational> {
    val quotient = (this / other).truncate()
    val remainder = this - other * quotient

    return quotient to remainder
}

fun BigRational.wholeNumberAndRemainder(): Pair<BigRational, BigRational> =
    divideAndRemainder(ONE)
