package hm.binkley.labs.skratch.math.algebra

interface FieldCompanion<T : Field<T>> : RingCompanion<T>

/** @todo Separate group-like from ring-like; division is a ring */
interface Field<T : Field<T>> : Ring<T> {
    override val companion: FieldCompanion<T>

    // No such thing as `operator unaryDiv`, ie, inverse
    fun unaryDiv(): T
    operator fun div(divisor: T): T = this * divisor.unaryDiv()
}
