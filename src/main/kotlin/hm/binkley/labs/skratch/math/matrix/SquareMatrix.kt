package hm.binkley.labs.skratch.math.matrix

abstract class SquareMatrix<N, Norm : GeneralNumber<Norm, Norm>, M>(
    val rank: Int,
    values: List<N>
) :
    GeneralMatrix<N, Norm, M>(rank, rank, values),
    Additive<M>,
    Multiplicative<M>,
    Scalable<M>
    where N : GeneralNumber<N, Norm>,
          M : SquareMatrix<N, Norm, M> {
    abstract val det: N // TODO: General algorithm for determinant
    val tr
        get() = (1..rank).map {
            this[it, it]
        }.reduce { acc, n -> acc + n }

    abstract val adj: M // TODO: adjoint vs adjugate

    override fun unaryDiv() = adj / det

    abstract fun isDiagonal(): Boolean
    abstract fun isSymmetric(): Boolean
    fun isSingular() = det.isZero()

    @Suppress("UNCHECKED_CAST")
    fun isIdempotent() = this == this as M * this

    @Suppress("UNCHECKED_CAST")
    fun isNilpotent() = (this as M * this).isZero()

    abstract fun isUpperTriangular(): Boolean
    abstract fun isLowerTriangular(): Boolean

    abstract fun symmetricPart(): M
    abstract fun antisymmetricPart(): M

    fun eigenvalues(): Pair<N, N> = TODO(
        "Formula at http://www.math" +
            ".harvard.edu/archive/21b_fall_04/exhibits/2dmatrices/ needs " +
            "square root"
    )
}
