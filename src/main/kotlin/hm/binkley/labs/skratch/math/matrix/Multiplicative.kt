package hm.binkley.labs.skratch.math.matrix

interface Multiplicative<T : Multiplicative<T>> {
    val multInv get() = unaryDiv()

    fun unaryDiv(): T

    operator fun times(other: T): T

    operator fun div(other: T): T

    fun isUnit(): Boolean
}
