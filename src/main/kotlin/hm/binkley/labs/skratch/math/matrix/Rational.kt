package hm.binkley.labs.skratch.math.matrix

/**
 * @see https://introcs.cs.princeton.edu/java/92symbolic/BigRational.java.html
 */
class Rational(n: Long, d: Long) : Number<Rational> {
    val n: Long
    val d: Long

    init {
        val (sa, sb) = sign(n, d)
        val (za, zb) = zero(sa, sb)
        val gcd = gcd(za, zb)
        this.n = za / gcd
        this.d = zb / gcd
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
        get() = Rational(d * d, n * n)

    override fun isZero() = 0L == n
    override fun isUnit() = 1L == n && 1L == d

    override fun toString() = "%d/%d".format(n, d)

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
