package hm.binkley.labs.skratch.layers.rules

import hm.binkley.labs.skratch.layers.Layers
import hm.binkley.labs.skratch.layers.ReversedSequence
import hm.binkley.labs.skratch.layers.Rule
import hm.binkley.labs.skratch.layers.getAsWithout

class CeilRule<
    K : Any,
    V : Any,
    // `where T : V` does not compile
    T : Comparable<T>
>(
    val ceiling: T
) : Rule<K, V, V>("ceil[$ceiling]") {
    @Suppress("UNCHECKED_CAST")
    override fun invoke(
        key: K,
        values: ReversedSequence<V>,
        layers: Layers<K, V, *>
    ): V = min(ceiling, layers.getAsWithout(key, this)) as V
}

/** Returns the lesser of [a] and [b], [a] on a tie for stable sorting. */
private fun <T : Comparable<T>> min(
    a: T,
    b: T
): T = if (b < a) b else a
