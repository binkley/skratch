package hm.binkley.labs.skratch.math.matrix

interface Additative<T : Additative<T>> {
    @Suppress("UNCHECKED_CAST")
    operator fun unaryPlus(): T = this as T

    operator fun unaryMinus(): T
    operator fun plus(that: T): T
    operator fun minus(that: T): T

    fun isZero(): Boolean
}
