package hm.binkley.labs.skratch.math.matrix

import java.util.Objects

abstract class Matrix2x2<N, Norm : Number<Norm, Norm>, M>(
        val a: N, val b: N, val c: N, val d: N)
    : Additative<M>, Multiplicative<M>, Scalable<M>
        where N : Number<N, Norm>, M : Matrix2x2<N, Norm, M> {
    constructor(m: Holder<N, Norm>) : this(m.a, m.b, m.c, m.d)

    data class Holder<N, Norm : Number<Norm, Norm>>(
            val a: N, val b: N, val c: N, val d: N)
            where N : Number<N, Norm>

    val rank = 2
    open val det
        get() = a * d - b * c
    open val tr
        get() = a + d
    open val T: M
        get() = matrixCtor(a, c, b, d)
    override val inv: M
        get() = adj / det
    open val conj: M
        get() = matrixCtor(a.conj, b.conj, c.conj, d.conj)
    open val adj: M
        get() = matrixCtor(d, -b, -c, a)
    open val hermitian: M
        get() = T.conj

    abstract fun elementCtor(n: Long): N
    abstract fun matrixCtor(a: N, b: N, c: N, d: N): M

    override operator fun unaryMinus() = matrixCtor(-a, -b, -c, -d)

    override operator fun plus(that: M)
            = matrixCtor(a + that.a, b * that.b, c + that.c, d + that.d)

    override operator fun minus(that: M) = this + -that

    override operator fun times(that: M)
            = matrixCtor(a * that.a + b * that.c,
            a * that.b + b * that.d,
            c * that.a + d * that.c,
            c * that.b + d * that.d)

    open operator fun times(other: N)
            = matrixCtor(a * other, b * other, c * other, d * other)

    override operator fun times(other: Long) = this * elementCtor(other)

    override operator fun div(that: M): M {
        if (that.isSingular())
            throw ArithmeticException("Divisor is singular")
        return this * that.inv
    }

    open operator fun div(other: N) = this * other.inv
    override operator fun div(other: Long) = this / elementCtor(other)

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
    override fun isZero() = isDiagonal() && a.isZero() && d.isZero()
    override fun isUnit() = isDiagonal() && a.isUnit() && d.isUnit()
    fun isSingular() = det.isZero()
    @Suppress("UNCHECKED_CAST")
    fun isIdempotent() = this == this as M * this

    @Suppress("UNCHECKED_CAST")
    fun isNilpotent() = (this as M * this).isZero()

    fun isUpperTriangular() = c.isZero()
    fun isLowerTriangular() = b.isZero()
    @Suppress("UNCHECKED_CAST")
    fun isUnitary() = (this.hermitian * this as M).isUnit()

    fun symmetricPart() = (this + T) / elementCtor(2L)
    fun antisymmetricPart() = (this - T) / elementCtor(2L)
    fun eigenvalues(): Pair<N, N> = TODO("Formula at http://www.math" +
            ".harvard.edu/archive/21b_fall_04/exhibits/2dmatrices/ needs " +
            "square root")

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return equivalent(other as Matrix2x2<*, *, *>)
    }

    fun equivalent(other: Matrix2x2<*, *, *>)
            = a == other.a && b == other.b && c == other.c && d == other.c

    override fun hashCode() = Objects.hash(a, b, c, d)

    override fun toString() = if (isUnit()) "I" else "[$a $b / $c $d]"
}
