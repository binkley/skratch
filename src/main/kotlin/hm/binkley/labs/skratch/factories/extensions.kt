package hm.binkley.labs.skratch.factories

infix fun <
    S : System<S>,
    V : Units<S, V, N>,
    N : Measure<S, V, N>
    >
Measure<S, *, *>.into(other: V): N = into(other) { it }

fun <
    S : System<S>,
    V : Units<S, V, N>,
    N : Measure<S, V, N>
    >
Measure<S, *, *>.into(
    other: V,
    conversion: (BigRational) -> BigRational,
): N = other.new(convertByBases(other, conversion))

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
    val least = into[leastIndex]!!
    // TODO: Reuse `+` operator
    into[leastIndex] = least.unit.new(least.quantity + current.quantity)

    return into.toNonNullableList() as List<Measure<S, *, *>>
}

private fun Measure<*, *, *>.convertByBases(
    other: Units<*, *, *>,
    conversion: (BigRational) -> BigRational,
) = conversion(quantity * unit.basis) / other.basis

private fun <T : Comparable<T>> Array<T>.sortedDescendingIndexed() =
    mapIndexed { index, it -> index to it }.sortedByDescending { it.second }

private fun <T> Collection<T?>.toNonNullableList() = map { it!! }
