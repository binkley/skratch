package hm.binkley.labs.skratch.factories

import java.util.Objects

abstract class System<S : System<S>>(
    val name: String
) {
    override fun toString() = name
}

sealed class Kind
object Length : Kind()
object Weight : Kind()

abstract class Units<
    S : System<S>,
    K : Kind,
    U : Units<S, K, U, M>,
    M : Measure<S, K, U, M>
    >(
    val system: S,
    val name: String,
    val basis: BigRational,
) : Comparable<Units<*, *, *, *>> {
    abstract fun new(value: BigRational): M
    override fun compareTo(other: Units<*, *, *, *>) =
        basis.compareTo(other.basis)
    override fun toString() = "$system $name@$basis"
}

abstract class Measure<
    S : System<S>,
    K : Kind,
    U : Units<S, K, U, M>,
    M : Measure<S, K, U, M>
    >(
    val unit: U,
    val quantity: BigRational,
) {
    // Member function so that explicit [M] type is not needed externally for
    // an extension function
    operator fun plus(other: Measure<S, K, *, *>): M =
        unit.new(quantity + (other into unit).quantity)

    override fun equals(other: Any?) = this === other ||
        other is Measure<*, *, *, *> &&
        unit == other.unit &&
        quantity == other.quantity

    override fun hashCode() = Objects.hash(unit, quantity)
    override fun toString() = "$quantity $unit"
}