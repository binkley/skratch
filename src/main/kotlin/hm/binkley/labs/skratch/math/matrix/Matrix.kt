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

    private inner class Row(private val row: Int) {
        val values: List<N>
            get() {
                val n = row - 1
                return this@Matrix.values.filterIndexed { index, _ ->
                    n == (index / cols)
                }
            }

        operator fun times(col: Col) = values.zip(col.values)
            .map { (a, b) -> a * b }
            .reduce { acc, it -> acc + it }
    }

    private inner class Col(private val col: Int) {
        val values: List<N>
            get() {
                val n = col - 1
                return this@Matrix.values.filterIndexed { index, _ ->
                    n == (index % cols)
                }
            }
    }

    override operator fun times(other: M): M {
        val values: MutableList<N> = ArrayList(values.size)
        for (i in 1..rows)
            for (j in 1..cols)
                values.add(Row(i) * Col(j))
        return matrixCtor(values)
    }

    open operator fun times(other: N) = matrixCtor(values.map {
        it * other
    })

    operator fun div(other: N): M = this * other.multInv

    fun isHermitian() = this == hermitian

    @Suppress("UNCHECKED_CAST")
    fun isUnitary() = (hermitian * this as M).isUnit()

    fun equivalent(other: Matrix<*, *, *>) =
        values.size == other.values.size &&
                values.zip(other.values) { a, b -> a.equivalent(b) }
                    .reduce { acc, it -> acc && it }
}
