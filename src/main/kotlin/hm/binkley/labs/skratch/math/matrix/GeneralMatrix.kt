package hm.binkley.labs.skratch.math.matrix

abstract class GeneralMatrix<N, Norm : GeneralNumber<Norm, Norm>, M>(
    val rows: Int,
    val cols: Int,
    private val values: List<N>,
) :
    Additive<M>,
    Multiplicative<M>,
    Scalable<M>
        where N : GeneralNumber<N, Norm>, // TODO: Conjugable
              M : GeneralMatrix<N, Norm, M> {
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

    private inner class Row(n: Int) : List<N> by row(n)

    private val Int.asRowNumber get() = (this / cols) + 1
    private fun row(row: Int) = values.filterIndexed { index, _ ->
        row == index.asRowNumber
    }

    private inner class Col(n: Int) : List<N> by col(n)

    private val Int.asColNumber get() = (this % cols) + 1
    private fun col(col: Int) = values.filterIndexed { index, _ ->
        col == index.asColNumber
    }

    private operator fun Row.times(col: Col) = this.zip(col)
        .map { (r, c) -> r * c }
        .reduce { acc, it -> acc + it }

    override operator fun times(other: M) = matrixCtor(
        (1..rows).map { Row(it) }
            .flatMap { row -> (1..cols).map { row to Col(it) } }
            .map { (row, col) -> row * col }
    )

    open operator fun times(other: N) = matrixCtor(
        values.map {
            it * other
        }
    )

    operator fun div(other: N): M = this * other.multInv

    fun isHermitian() = this == hermitian

    @Suppress("UNCHECKED_CAST")
    fun isUnitary() = (hermitian * this as M).isUnit()

    fun equivalent(other: GeneralMatrix<*, *, *>) =
        values.size == other.values.size &&
            values.zip(other.values) { a, b -> a.equivalent(b) }
                .reduce { acc, it -> acc && it }
}
