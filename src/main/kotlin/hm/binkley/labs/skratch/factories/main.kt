package hm.binkley.labs.skratch.factories

import hm.binkley.labs.skratch.factories.Bar.Bars
import hm.binkley.labs.skratch.factories.Baz.Bazs
import hm.binkley.labs.skratch.factories.Foo.Foos
import hm.binkley.labs.skratch.factories.Grok.Groks
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
    foo7.into(Bazs, Bars, Groks)
}

infix fun <
    S : System<S>,
    V : Units<S, V, N>,
    N : Measure<S, V, N>>
Measure<*, *, *>.into(other: V): N = other.new(convertByBases(other) { it })

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
    val value: BigRational,
) {
    // Member function so that explicit [M] type is not needed externally for
    // an extension function
    operator fun plus(other: Measure<*, *, *>): M =
        unit.new(value + (other into unit).value)

    override fun equals(other: Any?) = this === other ||
        other is Measure<*, *, *> &&
        unit == other.unit &&
        value == other.value

    override fun hashCode() = hash(unit, value)
    override fun toString() = "$value $unit"
}

object Meta : System<Meta>("Meta")

class Foo private constructor(
    value: BigRational,
) : Measure<Meta, Foos, Foo>(Foos, value) {
    companion object Foos : Units<Meta, Foos, Foo>(Meta, "foo", 1.rat) {
        override fun new(value: BigRational) = Foo(value)
    }
}

val BigRational.foo: Foo get() = Foos.new(this)
val Int.foo: Foo get() = rat.foo

class Bar private constructor(
    value: BigRational,
) : Measure<Meta, Bars, Bar>(Bars, value) {
    companion object Bars : Units<Meta, Bars, Bar>(Meta, "bar", 3.rat) {
        override fun new(value: BigRational) = Bar(value)
    }
}

val BigRational.bar: Bar get() = Bars.new(this)
val Int.bar: Bar get() = rat.bar

class Baz private constructor(
    value: BigRational,
) : Measure<Meta, Bazs, Baz>(Bazs, value) {
    companion object Bazs : Units<Meta, Bazs, Baz>(Meta, "baz", 9.rat) {
        override fun new(value: BigRational) = Baz(value)
    }
}

val BigRational.baz: Baz get() = Bazs.new(this)
val Int.baz: Baz get() = rat.baz

object Martian : System<Martian>("Martian")

class Grok private constructor(
    value: BigRational,
) : Measure<Martian, Groks, Grok>(Groks, value) {
    companion object Groks :
        Units<Martian, Groks, Grok>(Martian, "grok", 9.rat) {
        override fun new(value: BigRational) = Grok(value)
    }
}

fun Measure<*, *, *>.into(
    vararg units: Units<*, *, *>
): List<Measure<*, *, *>> {
    // TODO: How to make this a compile-time error?
    val system = unit.system
    units.map { it.system }.forEach {
        if (system != it) throw IllegalArgumentException(
            "Units from different systems: expected $system but got $it"
        )
    }

    // Pre-populate with nulls so that we may write in any order
    val into = MutableList<Measure<*, *, *>?>(units.size) { null }

    val descendingIndexed = units.sortedDescendingIndexed()
    var current = this
    descendingIndexed.forEach { (inputIndex, unit) ->
        val valueToReduce = current.convertByBases(unit) { it }
        val (whole, remainder) = valueToReduce.wholeNumberAndRemainder()
        into[inputIndex] = unit.new(whole)
        current = unit.new(remainder)
    }

    // Tack any left over into the least significant unit
    val leastIndex = descendingIndexed.last().first
    into[leastIndex] = into[leastIndex]!! + current

    return into.toNonNullableList()
}

private fun Measure<*, *, *>.convertByBases(
    other: Units<*, *, *>,
    conversion: (BigRational) -> BigRational,
) = conversion(value * unit.basis) / other.basis

private fun <T : Comparable<T>> Array<T>.sortedDescendingIndexed() =
    mapIndexed { index, it -> index to it }.sortedByDescending { it.second }

private fun <T> Collection<T?>.toNonNullableList() = map { it!! }
