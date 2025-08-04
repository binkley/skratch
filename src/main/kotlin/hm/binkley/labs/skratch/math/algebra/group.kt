package hm.binkley.labs.skratch.math.algebra

interface GroupConstants<T : Group<T>> : MonoidConstants<T>

/** @todo Additive inverse (minus) goes in a base type */
interface Group<T : Group<T>> : Monoid<T> {
    override val constants: GroupConstants<T>

    operator fun unaryMinus(): T

    operator fun minus(subtrahend: T): T = this + -subtrahend
}
