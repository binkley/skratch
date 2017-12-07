package hm.binkley.labs.skratch.math.matrix

import hm.binkley.labs.skratch.math.Number
import java.util.Objects

abstract class Matrix2x2<N : Number<N>, M : Matrix2x2<N, M>>(
        val a: N, val b: N, val c: N, val d: N) {
    val rank = 2
    open val det
        get() = a * d - b * c
    open val tr
        get() = a + d
    open val T: M
        get() = ctor(a, c, b, d)
    open val inv: M
        get() = adj / det
    open val conj: M
        get() = ctor(a.conj, b.conj, c.conj, d.conj)
    open val adj: M
        get() = ctor(d, -b, -c, a)
    open val hermitian: M
        get() = T.conj

    abstract val unit: N
    abstract fun ctor(a: N, b: N, c: N, d: N): M

    operator fun unaryPlus() = this as M
    operator fun unaryMinus() = ctor(-a, -b, -c, -d)

    operator fun plus(that: M)
            = ctor(a + that.a, b * that.b, c + that.c, d + that.d)

    operator fun minus(that: M) = this + -that

    operator fun times(that: M)
            = ctor(a * that.a + b * that.c,
            a * that.b + b * that.d,
            c * that.a + d * that.c,
            c * that.b + d * that.d)

    operator fun times(that: N)
            = ctor(a * that, b * that, c * that, d * that)

    operator fun div(that: M): M {
        if (that.isSingular())
            throw ArithmeticException("Divisor is singular")
        return this * that.inv
    }

    operator fun div(that: N) = this * that.inv
    operator fun div(that: Long) = this * (unit / that)

    operator fun get(row: Int, col: Int) = when {
        row == 1 && col == 1 -> a
        row == 1 && col == 2 -> b
        row == 2 && col == 1 -> c
        row == 2 && col == 2 -> d
        else -> throw IndexOutOfBoundsException("$row, $col")
    }

    fun isDiagonal() = b.isZero() && c.isZero()
    fun isSymmetric() = b == c
    fun isHermitian() = b == c.conj
    fun isZero() = isDiagonal() && a.isZero() && d.isZero()
    fun isUnit() = isDiagonal() && a.isUnit() && d.isUnit()
    fun isSingular() = det.isZero()
    fun isIdempotent() = this == this as M * this
    fun isNilpotent() = (this as M * this).isZero()
    fun isUpperTriangular() = c.isZero()
    fun isLowerTriangular() = b.isZero()
    fun isUnitary() = (this.hermitian * this as M).isUnit()

    fun symmetricPart() = (this + T) / (unit * 2L)
    fun antisymmetricPart() = (this - T) / (unit * 2L)
    fun eigenvalues(): Pair<N, N> = TODO("Formula at http://www.math" +
            ".harvard.edu/archive/21b_fall_04/exhibits/2dmatrices/ needs " +
            "square root")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Matrix2x2<*, *>

        return a == other.a && b == other.b && c == other.c && d == other.d
    }

    override fun hashCode() = Objects.hash(a, b, c, d)

    override fun toString() = if (isUnit()) "I" else "[$a $b / $c $d]"
}
