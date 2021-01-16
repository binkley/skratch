package hm.binkley.labs.skratch.math.matrix

interface Conjugable<T : Conjugable<T>> {
    val conj: T
}
