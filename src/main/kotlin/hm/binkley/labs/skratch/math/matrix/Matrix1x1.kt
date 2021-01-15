package hm.binkley.labs.skratch.math.matrix

import java.util.Objects.hash

interface HasA<N, Norm : GeneralNumber<Norm, Norm>, M>
        where N : GeneralNumber<N, Norm>,
              M : SquareMatrix<N, Norm, M> {
    val a: N
}

abstract class Matrix1x1<N, Norm : GeneralNumber<Norm, Norm>, M>(
    a: N,
) :
    SquareMatrix<N, Norm, M>(1, listOf(a)),
    HasA<N, Norm, M>
        where N : GeneralNumber<N, Norm>,
              M : Matrix1x1<N, Norm, M> {
    constructor(m: Holder<N, Norm>) : this(m.a)

    data class Holder<N, Norm : GeneralNumber<Norm, Norm>>(
        val a: N,
    ) where N : GeneralNumber<N, Norm>

    override val a: N get() = this[1, 1]

    override val det get() = a

    override val conj: M get() = matrixCtor(a.conj)
    override val T get() = matrixCtor(a)
    override val adj: M get() = matrixCtor(a)

    protected abstract fun elementCtor(n: Long): N
    protected abstract fun matrixCtor(a: N): M
    override fun matrixCtor(values: List<N>) = matrixCtor(values[0])

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

    override fun isDiagonal() = true
    override fun isSymmetric() = true
    override fun isZero() = isDiagonal() && a.isZero()
    override fun isUnit() = isDiagonal() && a.isUnit()

    override fun isUpperTriangular() = true
    override fun isLowerTriangular() = true

    override fun symmetricPart() = (this + T) / elementCtor(2L)
    override fun antisymmetricPart() = (this - T) / elementCtor(2L)

    @Suppress("UNCHECKED_CAST")
    override fun equals(other: Any?) = this === other ||
            javaClass == other?.javaClass &&
            equivalent(other as M)

    override fun hashCode() = hash(a)

    override fun toString() = if (isUnit()) "I" else "[$a]"
}
