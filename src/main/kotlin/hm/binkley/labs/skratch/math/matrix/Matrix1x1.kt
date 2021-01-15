package hm.binkley.labs.skratch.math.matrix

import java.util.Objects.hash

interface HasA<N, Norm : GeneralNumber<Norm, Norm>, M>
        where N : GeneralNumber<N, Norm>,
              M : SquareMatrix<N, Norm, M> {
    val a: N
}

abstract class Matrix1x1<N, Norm : GeneralNumber<Norm, Norm>, M>(
    override val a: N,
) :
    SquareMatrix<N, Norm, M>(1),
    HasA<N, Norm, M>
        where N : GeneralNumber<N, Norm>,
              M : Matrix1x1<N, Norm, M> {
    constructor(m: Holder<N, Norm>) : this(m.a)

    data class Holder<N, Norm : GeneralNumber<Norm, Norm>>(
        val a: N,
    ) where N : GeneralNumber<N, Norm>

    override val det get() = a
    override val tr get() = a

    override val conjugate get() = matrixCtor(a.conjugate)
    override val T get() = matrixCtor(a)
    override val adj get() = matrixCtor(a)

    abstract fun elementCtor(n: Long): N
    abstract fun matrixCtor(a: N): M

    override operator fun unaryMinus() = matrixCtor(-a)
    override operator fun plus(other: M) = matrixCtor(a + other.a)
    override operator fun times(other: M) = matrixCtor(a * other.a)
    override operator fun times(other: N) = matrixCtor(a * other)
    override operator fun times(other: Long) = this * elementCtor(other)

    override operator fun div(other: M): M =
        if (other.isSingular())
            throw ArithmeticException("Divisor is singular")
        else this * other.multInv

    override operator fun div(other: Long) = this / elementCtor(other)

    operator fun get(row: Int, col: Int) = when {
        row == 1 && col == 1 -> a
        else -> throw IndexOutOfBoundsException(
            "Matrices use 1-based indexing: $row, $col")
    }

    override fun isDiagonal() = true
    override fun isSymmetric() = true
    override fun isHermitian() = true
    override fun isZero() = isDiagonal() && a.isZero()
    override fun isUnit() = isDiagonal() && a.isUnit()

    override fun isUpperTriangular() = true
    override fun isLowerTriangular() = true

    override fun symmetricPart() = (this + T) / elementCtor(2L)
    override fun antisymmetricPart() = (this - T) / elementCtor(2L)

    fun equivalent(other: Matrix1x1<*, *, *>) = a.equivalent(other.a)

    @Suppress("UNCHECKED_CAST")
    override fun equals(other: Any?) = this === other ||
            javaClass == other?.javaClass &&
            equivalent(other as M)

    override fun hashCode() = hash(a)

    override fun toString() = if (isUnit()) "I" else "[$a]"
}
