package hm.binkley.labs.skratch.math.matrix

interface Scalable<T : Scalable<T>> {
    operator fun times(other: Long): T

    operator fun div(other: Long): T
}
