package hm.binkley.labs.skratch.math.matrix

abstract class Matrix<N, Norm : GeneralNumber<Norm, Norm>, M>(
    val rows: Int,
    val cols: Int,
    private val values: List<N>,
) :
    Additive<M>,
    Multiplicative<M>,
    Scalable<M>
        where N : GeneralNumber<N, Norm>, // TODO: Conjugable
              M : Matrix<N, Norm, M> {
    abstract val conj: M // TODO: Implement conjugate algorithm
    abstract val T: M // TODO: Implement transpose algorithm
    val hermitian get() = T.conj

    protected abstract fun matrixCtor(values: List<N>): M

    override fun unaryMinus() = matrixCtor(values.map { -it })
    override fun plus(other: M) =
        matrixCtor(values.zip(other.values) { a, b -> a + b })

    /** 1-based row/column indexing. */
    operator fun get(row: Int, col: Int) =
        values[(row - 1) * cols + (col - 1)]

    abstract operator fun times(other: N): M
    operator fun div(other: N): M = this * other.multInv

    fun isHermitian() = this == hermitian

    @Suppress("UNCHECKED_CAST")
    fun isUnitary() = (hermitian * this as M).isUnit()

    fun equivalent(other: Matrix<*, *, *>) =
        values.size == other.values.size &&
                values.zip(other.values) { a, b -> a.equivalent(b) }
                    .reduce { acc, it -> acc && it }
}
