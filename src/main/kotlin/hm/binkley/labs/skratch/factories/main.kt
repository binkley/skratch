package hm.binkley.labs.skratch.factories

import hm.binkley.labs.skratch.factories.Bar.Bars
import hm.binkley.labs.skratch.factories.Baz.Bazs
import hm.binkley.labs.skratch.factories.Foo.Foos
import java.util.Objects.hash

fun main() {
    val foo1 = 1.foo
    val foo2 = 2.foo
    val bar3 = 3.bar

    println("== FACTORIES")
    println(foo1)
    println(foo2)
    println(bar3)

    val list1 = listOf(foo1, foo2, bar3)
    val sum1 = list1.fold(0.foo) { acc, it -> acc + it }
    println("SUM #1 -> $sum1 $list1")

    val bar2 = foo1 into Bars
    println(bar2)
    val list2 = listOf(foo1, foo2, bar2)
    val sum2 = list2.fold(0.foo) { acc, it -> acc + it }
    println("SUM #2 -> $sum2 $list2")

    val foo14 = 14.foo
    println(foo14)
    val wholes = foo14.into(Foos, Bazs, Bars)
    val sum3 = wholes.fold(0.foo) { acc, it -> acc + it }
    println("SUM #3 -> $sum3 $wholes")

    val foo14_2 = (29 over 2).foo
    println(foo14_2)
    val remainders = foo14_2.into(Foos, Bazs, Bars)
    val sum4 = remainders.fold(0.foo) { acc, it -> acc + it }
    println("SUM #4 -> $sum4 $remainders")

    val foo7 = 14.foo
    println(foo7)
    // Correctly does not compile -- Groks is in the wrong System
    // foo7.into(Bazs, Bars, Groks)
}

abstract class System<S : System<S>>(
    val name: String
) {
    override fun toString() = name
}

abstract class Units<
    S : System<S>,
    U : Units<S, U, M>,
    M : Measure<S, U, M>>(
    val system: S,
    val name: String,
    val basis: BigRational,
) : Comparable<Units<*, *, *>> {
    abstract fun new(value: BigRational): M
    override fun compareTo(other: Units<*, *, *>) =
        basis.compareTo(other.basis)
    override fun toString() = "$system $name@$basis"
}

abstract class Measure<
    S : System<S>,
    U : Units<S, U, M>,
    M : Measure<S, U, M>>(
    val unit: U,
    val quantity: BigRational,
) {
    // Member function so that explicit [M] type is not needed externally for
    // an extension function
    operator fun plus(other: Measure<*, *, *>): M =
        unit.new(quantity + (other into unit).quantity)

    override fun equals(other: Any?) = this === other ||
        other is Measure<*, *, *> &&
        unit == other.unit &&
        quantity == other.quantity

    override fun hashCode() = hash(unit, quantity)
    override fun toString() = "$quantity $unit"
}

infix fun <
    S : System<S>,
    V : Units<S, V, N>,
    N : Measure<S, V, N>>
Measure<*, *, *>.into(other: V): N = other.new(convertByBases(other) { it })

@Suppress("UNCHECKED_CAST")
fun <S : System<S>> Measure<S, *, *>.into(
    vararg units: Units<S, *, *>
): List<Measure<S, *, *>> {
    // Pre-populate with nulls so that we may write in any order
    val into = MutableList<Measure<*, *, *>?>(units.size) { null }

    val descendingIndexed = units.sortedDescendingIndexed()
    var current: Measure<S, *, *> = this
    descendingIndexed.forEach { (inputIndex, unit) ->
        val valueToReduce = current.convertByBases(unit) { it }
        val (whole, remainder) = valueToReduce.wholeNumberAndRemainder()
        into[inputIndex] = unit.new(whole)
        current = unit.new(remainder) as Measure<S, *, *>
    }

    // Tack any left over into the least significant unit
    val leastIndex = descendingIndexed.last().first
    into[leastIndex] = into[leastIndex]!! + current

    return into.toNonNullableList() as List<Measure<S, *, *>>
}

private fun Measure<*, *, *>.convertByBases(
    other: Units<*, *, *>,
    conversion: (BigRational) -> BigRational,
) = conversion(quantity * unit.basis) / other.basis

private fun <T : Comparable<T>> Array<T>.sortedDescendingIndexed() =
    mapIndexed { index, it -> index to it }.sortedByDescending { it.second }

private fun <T> Collection<T?>.toNonNullableList() = map { it!! }
