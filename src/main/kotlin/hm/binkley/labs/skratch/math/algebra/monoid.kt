package hm.binkley.labs.skratch.math.algebra

/** @todo Kotlin assumes left/right identity, never one-sided */
interface MonoidCompanion<T : Monoid<T>> {
    @Suppress("PropertyName")
    val ZERO: T
}

/** @todo Study Arrow -- it provides many algebraic structures */
interface Monoid<T : Monoid<T>> {
    val companion: MonoidCompanion<T>

    @Suppress("UNCHECKED_CAST")
    operator fun unaryPlus(): T = this as T
    operator fun plus(addend: T): T
}
