package hm.binkley.labs.skratch.math.algebra

interface SemigroupConstants<T : Semigroup<T>> : MagmaConstants<T>

interface Semigroup<T : Semigroup<T>> : Magma<T> {
    override val constants: SemigroupConstants<T>
}
