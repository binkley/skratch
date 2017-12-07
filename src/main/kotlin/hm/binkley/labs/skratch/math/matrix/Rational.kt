package hm.binkley.labs.skratch.math.matrix

class Rational(n: Long, d: Long) : Number<Rational> {
    val n: Long
    val d: Long

    init {
        val (a, b) = sign(n, d)
        val gcm = gcm(a, b)
        this.n = a / gcm
        this.d = b / gcm
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

    override fun isZero() = 0L == n
    override fun isUnit() = 1L == n && 1L == d

    override fun toString() = "%d/%d".format(n, d)

    companion object {
        private fun sign(a: Long, b: Long)
                = if (b < 0) -a to -b else a to b

        private tailrec fun gcm(a: Long, b: Long): Long
                = if (b == 0L) a else gcm(b, a % b)
    }
}
