package hm.binkley.labs.skratch.math.algebra

interface FieldConstants<T : Field<T>> : RingConstants<T>

/** @todo Separate group-like from ring-like; division is a ring */
interface Field<T : Field<T>> : Ring<T> {
    override val constants: FieldConstants<T>

    // No such thing as `operator unaryDiv`, ie, inverse
    fun unaryDiv(): T
    operator fun div(divisor: T): T = this * divisor.unaryDiv()
}
