package hm.binkley.labs.skratch.math.matrix

interface Additive<T : Additive<T>> {
    val additiveInverse get() = unaryMinus()

    @Suppress("UNCHECKED_CAST")
    operator fun unaryPlus(): T = this as T

    operator fun unaryMinus(): T
    operator fun plus(other: T): T
    operator fun minus(other: T): T

    fun isZero(): Boolean
}
