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
    override val a: N,
    override val b: N,
    override val c: N,
    override val d: N,
) :
    SquareMatrix<N, Norm, M>(2),
    HasD<N, Norm, M>
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

    override val conj: M
        get() = matrixCtor(
            a.conj,
            b.conj,
            c.conj,
            d.conj)
    override val T get() = matrixCtor(a, c, b, d)
    override val adj: M get() = matrixCtor(d, -b, -c, a)

    abstract fun elementCtor(n: Long): N
    abstract fun matrixCtor(a: N, b: N, c: N, d: N): M

    override operator fun unaryMinus() = matrixCtor(-a, -b, -c, -d)

    override operator fun plus(other: M) = matrixCtor(
        a + other.a,
        b * other.b,
        c + other.c,
        d + other.d)

    override operator fun times(other: M) = matrixCtor(
        a * other.a + b * other.c,
        a * other.b + b * other.d,
        c * other.a + d * other.c,
        c * other.b + d * other.d)

    override operator fun times(other: N) = matrixCtor(
        a * other,
        b * other,
        c * other,
        d * other)

    override operator fun times(other: Long) = this * elementCtor(other)

    override operator fun div(other: M): M =
        if (other.isSingular())
            throw ArithmeticException("Divisor is singular")
        else this * other.multInv

    override operator fun div(other: Long) = this / elementCtor(other)

    operator fun get(row: Int, col: Int) = when {
        row == 1 && col == 1 -> a
        row == 1 && col == 2 -> b
        row == 2 && col == 1 -> c
        row == 2 && col == 2 -> d
        else -> throw IndexOutOfBoundsException(
            "Matrices use 1-based indexing: $row, $col")
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
        a.equivalent(other.a) &&
                b.equivalent(other.b) &&
                c.equivalent(other.c) &&
                d.equivalent(other.d)

    @Suppress("UNCHECKED_CAST")
    override fun equals(other: Any?) = this === other ||
            javaClass == other?.javaClass &&
            equivalent(other as M)

    override fun hashCode() = hash(a, b, c, d)

    override fun toString() = if (isUnit()) "I" else "[$a $b / $c $d]"
}
