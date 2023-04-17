package hm.binkley.labs.skratch.math.matrix

import java.util.Objects.hash

interface HasB<N, Norm : GeneralNumber<Norm, Norm>, M> :
    HasA<N, Norm, M>
    where N : GeneralNumber<N, Norm>,
          M : SquareMatrix<N, Norm, M> {
    val b: N
}

interface HasC<N, Norm : GeneralNumber<Norm, Norm>, M> :
    HasB<N, Norm, M>
    where N : GeneralNumber<N, Norm>,
          M : SquareMatrix<N, Norm, M> {
    val c: N
}

interface HasD<N, Norm : GeneralNumber<Norm, Norm>, M> :
    HasC<N, Norm, M>
    where N : GeneralNumber<N, Norm>,
          M : SquareMatrix<N, Norm, M> {
    val d: N
}

abstract class Matrix2x2<N, Norm : GeneralNumber<Norm, Norm>, M>(
    a: N,
    b: N,
    c: N,
    d: N
) :
    SquareMatrix<N, Norm, M>(2, listOf(a, b, c, d)),
    HasD<N, Norm, M>
    where N : GeneralNumber<N, Norm>,
          M : Matrix2x2<N, Norm, M> {
    constructor(m: Holder<N, Norm>) : this(m.a, m.b, m.c, m.d)

    data class Holder<N, Norm : GeneralNumber<Norm, Norm>>(
        val a: N,
        val b: N,
        val c: N,
        val d: N
    ) where N : GeneralNumber<N, Norm>

    override val a: N get() = this[1, 1]
    override val b: N get() = this[1, 2]
    override val c: N get() = this[2, 1]
    override val d: N get() = this[2, 2]

    override val det get() = a * d - b * c

    override val conj: M
        get() = matrixCtor(
            a.conj,
            b.conj,
            c.conj,
            d.conj
        )
    override val T get() = matrixCtor(a, c, b, d)
    override val adj: M get() = matrixCtor(d, -b, -c, a)

    protected abstract fun elementCtor(n: Long): N
    protected abstract fun matrixCtor(a: N, b: N, c: N, d: N): M
    override fun matrixCtor(values: List<N>) =
        matrixCtor(values[0], values[1], values[2], values[3])

    override operator fun unaryMinus() = matrixCtor(-a, -b, -c, -d)

    override operator fun plus(other: M) = matrixCtor(
        a + other.a,
        b * other.b,
        c + other.c,
        d + other.d
    )

    override operator fun times(other: Long) = this * elementCtor(other)

    override operator fun div(other: M): M =
        if (other.isSingular()) {
            throw ArithmeticException("Divisor is singular")
        } else {
            this * other.multInv
        }

    override operator fun div(other: Long) = this / elementCtor(other)

    override fun isDiagonal() = b.isZero() && c.isZero()
    override fun isSymmetric() = b == c
    override fun isZero() = isDiagonal() && a.isZero() && d.isZero()
    override fun isUnit() = isDiagonal() && a.isUnit() && d.isUnit()

    override fun isUpperTriangular() = c.isZero()
    override fun isLowerTriangular() = b.isZero()

    override fun symmetricPart() = (this + T) / elementCtor(2L)
    override fun antisymmetricPart() = (this - T) / elementCtor(2L)

    @Suppress("UNCHECKED_CAST")
    override fun equals(other: Any?) = this === other ||
        javaClass == other?.javaClass &&
        equivalent(other as M)

    override fun hashCode() = hash(a, b, c, d)

    override fun toString() = if (isUnit()) "I" else "[$a $b / $c $d]"
}
