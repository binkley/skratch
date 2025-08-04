package hm.binkley.labs.skratch.math.matrix

interface Additive<T : Additive<T>> {
    val addInv get() = -this

    @Suppress("UNCHECKED_CAST")
    operator fun unaryPlus() = this as T

    operator fun unaryMinus(): T

    operator fun plus(other: T): T

    operator fun minus(other: T) = this + -other

    fun isZero(): Boolean
}
