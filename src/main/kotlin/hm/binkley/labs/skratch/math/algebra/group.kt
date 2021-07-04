package hm.binkley.labs.skratch.math.algebra

interface GroupCompanion<T : Group<T>> : MonoidCompanion<T>

/** @todo Additive inverse (minus) goes in a base type */
interface Group<T : Group<T>> : Monoid<T> {
    override val companion: GroupCompanion<T>

    operator fun unaryMinus(): T
    operator fun minus(subtrahend: T): T = this + -subtrahend
}
