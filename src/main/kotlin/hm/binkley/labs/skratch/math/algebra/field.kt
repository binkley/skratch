package hm.binkley.labs.skratch.math.algebra

interface FieldConstants<T : Field<T>> : RingConstants<T>

/** @todo Separate group-like from ring-like; division is a ring */
interface Field<T : Field<T>> : Ring<T> {
    override val constants: FieldConstants<T>

    /**
     * No such thing as `operator unaryDiv`, ie, inverse.
     *
     * @todo Kotlin does not have true "traits", meaning, to provide
     *       implementation for, say, division by ZERO as a base case needs
     *       a class rather than interface, and "doX" patterns for subtypes
     *       to implement for non-base cases
     */
    fun unaryDiv(): T

    operator fun div(divisor: T): T = this * divisor.unaryDiv()
}
