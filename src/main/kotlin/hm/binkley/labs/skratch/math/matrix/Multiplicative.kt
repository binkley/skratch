package hm.binkley.labs.skratch.math.matrix

interface Multiplicative<T : Multiplicative<T>> {
    operator fun times(other: T): T
    operator fun div(other: T): T
    val inv: T

    fun isUnit(): Boolean
}
