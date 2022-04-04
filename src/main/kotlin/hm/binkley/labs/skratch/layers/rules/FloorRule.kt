package hm.binkley.labs.skratch.layers.rules

import hm.binkley.labs.skratch.layers.Layers
import hm.binkley.labs.skratch.layers.ReversedSequence
import hm.binkley.labs.skratch.layers.Rule
import hm.binkley.labs.skratch.layers.getAsWithout

class FloorRule<
    K : Any,
    V : Any,
    T : Comparable<T>, // `where T : V` does not compile
    >(val floor: T) : Rule<K, V, V>("floor[$floor]") {
    @Suppress("UNCHECKED_CAST")
    override fun invoke(
        key: K,
        values: ReversedSequence<V>,
        layers: Layers<K, V, *>,
    ): V = max(floor, layers.getAsWithout(key, this)) as V
}

/** Returns the greater of [a] and [b], [a] on a tie for stable sorting. */
private fun <T : Comparable<T>> max(a: T, b: T): T = if (b > a) b else a
