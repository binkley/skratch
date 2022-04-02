package hm.binkley.labs.skratch.generics

infix fun <
    S : System<S>,
    K : Kind,
    V : Units<S, K, V, N>,
    N : Measure<S, K, V, N>
    >
Measure<S, K, *, *>.into(other: V): N = into(other) { it }

fun <
    S : System<S>,
    K : Kind,
    V : Units<S, K, V, N>,
    N : Measure<S, K, V, N>
    >
Measure<S, K, *, *>.into(
    other: V,
    conversion: (BigRational) -> BigRational,
): N = other.new(convertByBases(other, conversion))

fun <S : System<S>, K : Kind>
Measure<S, K, *, *>.into(
    units: List<Units<S, K, *, *>>,
): List<Measure<S, K, *, *>> {
    // Pre-populate with nulls so that we may write in any order
    val into = MutableList<Measure<*, *, *, *>?>(units.size) { null }

    val descendingIndexed = units.sortedDescendingIndexed()
    var current: Measure<*, *, *, *> = this
    descendingIndexed.forEach { (inputIndex, unit) ->
        val valueToReduce = current.convertByBases(unit) { it }
        val (whole, remainder) = valueToReduce.wholeNumberAndRemainder()
        into[inputIndex] = unit.new(whole)
        current = unit.new(remainder)
    }

    // Tack any left over into the least significant unit
    val leastIndex = descendingIndexed.last().first
    val least = into[leastIndex]!!
    // TODO: Reuse `+` operator`
    into[leastIndex] = least.unit.new(least.quantity + current.quantity)

    @Suppress("UNCHECKED_CAST")
    return into.toNonNullableList() as List<Measure<S, K, *, *>>
}

fun <S : System<S>, K : Kind>
Measure<S, K, *, *>.into(
    vararg units: Units<S, K, *, *>,
): List<Measure<S, K, *, *>> = into(units.asList())

private fun Measure<*, *, *, *>.convertByBases(
    other: Units<*, *, *, *>,
    conversion: (BigRational) -> BigRational,
) = conversion(quantity * unit.basis) / other.basis

private fun <T : Comparable<T>> List<T>.sortedDescendingIndexed() =
    mapIndexed { index, it -> index to it }.sortedByDescending { it.second }

private fun <T> Collection<T?>.toNonNullableList() = map { it!! }
