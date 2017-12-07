package hm.binkley.labs.skratch.math.matrix

interface Number<N : Number<N>> {
    @Suppress("UNCHECKED_CAST")
    operator fun unaryPlus(): N = this as N

    operator fun unaryMinus(): N
    operator fun plus(that: N): N
    operator fun minus(that: N): N
    operator fun times(that: N): N
    operator fun times(that: Long): N
    operator fun div(that: N): N
    operator fun div(that: Long): N
    val inv: N
    val conj: N
    val abs: N

    fun isZero(): Boolean
    fun isUnit(): Boolean
}
