package hm.binkley.labs.skratch.math.algebra

/** @todo Kotlin assumes left/right identity, never one-sided */
interface MonoidConstants<T : Monoid<T>> : SemigroupConstants<T> {
    @Suppress("PropertyName")
    val ZERO: T
}

/** @todo Study Arrow and Kmath -- it provides many algebraic structures */
interface Monoid<T : Monoid<T>> : Semigroup<T> {
    override val constants: MonoidConstants<T>

    @Suppress("UNCHECKED_CAST")
    operator fun unaryPlus(): T = this as T

    operator fun plus(addend: T): T
}
