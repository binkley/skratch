package hm.binkley.labs.skratch.math.matrix

import java.util.Objects

abstract class Matrix2x2<N, Norm : GeneralNumber<Norm, Norm>, M>(
    val a: N,
    val b: N,
    val c: N,
    val d: N,
) :
    SquareMatrix<N, Norm, M>(2),
    Additive<M>,
    Multiplicative<M>,
    Scalable<M>
        where N : GeneralNumber<N, Norm>,
              M : Matrix2x2<N, Norm, M> {
    constructor(m: Holder<N, Norm>) : this(m.a, m.b, m.c, m.d)

    data class Holder<N, Norm : GeneralNumber<Norm, Norm>>(
        val a: N,
        val b: N,
        val c: N,
        val d: N,
    ) where N : GeneralNumber<N, Norm>

    override val det get() = a * d - b * c
    override val tr get() = a + d
    open val T: M get() = matrixCtor(a, c, b, d)
    override val multiplicativeInverse: M get() = adj / det
    override val conj: M get() = matrixCtor(a.conj, b.conj, c.conj, d.conj)
    override val adj: M get() = matrixCtor(d, -b, -c, a)
    override val hermitian: M get() = T.conj

    abstract fun elementCtor(n: Long): N
    abstract fun matrixCtor(a: N, b: N, c: N, d: N): M

    override operator fun unaryMinus() = matrixCtor(-a, -b, -c, -d)

    override operator fun plus(other: M) = matrixCtor(a + other.a,
        b * other.b, c + other.c, d + other.d)

    override operator fun minus(other: M) = this + -other

    override operator fun times(other: M) = matrixCtor(
        a * other.a + b * other.c,
        a * other.b + b * other.d,
        c * other.a + d * other.c,
        c * other.b + d * other.d)

    open operator fun times(other: N) = matrixCtor(a * other, b * other,
        c * other, d * other)

    override operator fun times(other: Long) = this * elementCtor(other)

    override operator fun div(other: M): M {
        if (other.isSingular())
            throw ArithmeticException("Divisor is singular")
        return this * other.multiplicativeInverse
    }

    open operator fun div(other: N) = this * other.multiplicativeInverse
    override operator fun div(other: Long) = this / elementCtor(other)

    operator fun get(row: Int, col: Int) = when {
        row == 1 && col == 1 -> a
        row == 1 && col == 2 -> b
        row == 2 && col == 1 -> c
        row == 2 && col == 2 -> d
        else -> throw IndexOutOfBoundsException("$row, $col")
    }

    override fun isDiagonal() = b.isZero() && c.isZero()
    override fun isSymmetric() = b == c
    override fun isHermitian() = b == c.conj
    override fun isZero() = isDiagonal() && a.isZero() && d.isZero()
    override fun isUnit() = isDiagonal() && a.isUnit() && d.isUnit()

    override fun isUpperTriangular() = c.isZero()
    override fun isLowerTriangular() = b.isZero()

    override fun symmetricPart() = (this + T) / elementCtor(2L)
    override fun antisymmetricPart() = (this - T) / elementCtor(2L)

    fun equivalent(other: Matrix2x2<*, *, *>) =
        a.equivalent(other.a)
                && b.equivalent(other.b)
                && c.equivalent(other.c)
                && d.equivalent(other.d)

    @Suppress("UNCHECKED_CAST")
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return equivalent(other as M)
    }

    override fun hashCode() = Objects.hash(a, b, c, d)

    override fun toString() = if (isUnit()) "I" else "[$a $b / $c $d]"
}
