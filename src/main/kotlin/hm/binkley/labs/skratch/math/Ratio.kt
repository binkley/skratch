package hm.binkley.labs.skratch.math

import java.util.Objects

class Ratio(n: Long, d: Long) : Rational<Ratio> {
    val n: Long
    val d: Long

    init {
        val gcm = gcm(n, d)
        this.n = n / gcm
        this.d = d / gcm
    }

    constructor(n: Long) : this(n, 1)

    override operator fun unaryMinus() = Ratio(-n, d)

    override operator fun plus(that: Ratio)
            = Ratio(n * that.d + that.n * d,
            d * that.d)

    operator fun plus(that: Long) = this + Ratio(
            that)

    override operator fun minus(that: Ratio) = this + -that
    operator fun minus(that: Long) = this + -that

    override operator fun times(that: Ratio) = Ratio(
            n * that.n, d * that.d)

    operator fun times(that: Long) = this * Ratio(
            that)

    override operator fun div(that: Ratio) = this * that.inv
    operator fun div(that: Long) = this / Ratio(
            that)

    val inv by lazy { Ratio(d, n) }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Ratio

        return n == other.n && d == other.d
    }

    override fun hashCode() = Objects.hash(n, d)

    override fun toString() = if (1L == d) n.toString() else "$n/$d"

    companion object {
        private tailrec fun gcm(a: Long, b: Long): Long
                = if (b == 0L) a else gcm(b, a % b)
    }
}

operator fun Long.plus(that: Ratio) = Ratio(this) + that
operator fun Long.minus(that: Ratio) = Ratio(this) - that
operator fun Long.times(that: Ratio) = Ratio(this) * that
operator fun Long.div(that: Ratio) = Ratio(this) / that
