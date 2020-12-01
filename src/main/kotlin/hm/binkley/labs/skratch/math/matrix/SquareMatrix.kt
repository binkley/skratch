package my.stuff.hm.binkley.labs.skratch.math.matrix

import hm.binkley.labs.skratch.math.matrix.Additive
import hm.binkley.labs.skratch.math.matrix.GeneralNumber
import hm.binkley.labs.skratch.math.matrix.Multiplicative
import hm.binkley.labs.skratch.math.matrix.Scalable

abstract class SquareMatrix<N, Norm : GeneralNumber<Norm, Norm>, M>(
    val rank: Int,
) :
    Additive<M>,
    Multiplicative<M>,
    Scalable<M>
        where N : GeneralNumber<N, Norm>,
              M : SquareMatrix<N, Norm, M> {
    abstract val det: N
    abstract val tr: N
    abstract val conj: M
    abstract val adj: M
    abstract val hermitian: M

    abstract fun isDiagonal(): Boolean
    abstract fun isSymmetric(): Boolean
    abstract fun isHermitian(): Boolean
    fun isSingular() = det.isZero()

    @Suppress("UNCHECKED_CAST")
    fun isIdempotent() = this == this as M * this

    @Suppress("UNCHECKED_CAST")
    fun isNilpotent() = (this as M * this).isZero()

    abstract fun isUpperTriangular(): Boolean
    abstract fun isLowerTriangular(): Boolean

    @Suppress("UNCHECKED_CAST")
    fun isUnitary() = (this.hermitian * this as M).isUnit()

    abstract fun symmetricPart(): M
    abstract fun antisymmetricPart(): M

    fun eigenvalues(): Pair<N, N> = TODO("Formula at http://www.math" +
            ".harvard.edu/archive/21b_fall_04/exhibits/2dmatrices/ needs " +
            "square root")
}
