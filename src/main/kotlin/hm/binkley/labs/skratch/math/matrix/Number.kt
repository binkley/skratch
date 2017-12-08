package hm.binkley.labs.skratch.math.matrix

interface Number<T, Norm>
        where T : Number<T, Norm>, Norm : Number<Norm, Norm> {
    @Suppress("UNCHECKED_CAST")
    operator fun unaryPlus(): T = this as T

    operator fun unaryMinus(): T
    operator fun plus(that: T): T
    operator fun minus(that: T): T
    operator fun times(that: T): T
    operator fun times(that: Long): T
    operator fun div(that: T): T
    operator fun div(that: Long): T
    val inv: T
    val conj: T
    val abs: Norm
    val sqnorm: Norm

    fun isZero(): Boolean
    fun isUnit(): Boolean
}
