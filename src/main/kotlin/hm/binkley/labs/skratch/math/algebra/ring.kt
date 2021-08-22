package hm.binkley.labs.skratch.math.algebra

interface RingConstants<T : Ring<T>> : GroupConstants<T> {
    @Suppress("PropertyName")
    val ONE: T
}

interface Ring<T : Ring<T>> : Group<T> {
    override val constants: RingConstants<T>

    operator fun times(factor: T): T
}
