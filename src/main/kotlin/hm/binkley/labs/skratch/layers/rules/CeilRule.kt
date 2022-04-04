package hm.binkley.labs.skratch.layers.rules

import hm.binkley.labs.skratch.layers.Layers
import hm.binkley.labs.skratch.layers.ReversedSequence
import hm.binkley.labs.skratch.layers.Rule
import hm.binkley.labs.skratch.layers.layerFor

class CeilRule<
    K : Any,
    V : Any,
    T : Comparable<T>, // `where T : V` does not compile
    >(val ceiling: T) : Rule<K, V, V>("ceil[$ceiling]") {
    @Suppress("UNCHECKED_CAST")
    override fun invoke(
        key: K,
        values: ReversedSequence<V>,
        layers: Layers<K, V, *>,
    ): V = min(ceiling, valueFor(key, layers)) as V
}

private fun <K : Any, V : Any, T> Rule<K, V, *>.valueFor(
    key: K,
    layers: Layers<K, V, *>
): T = layers.whatIfNot(layerFor(key, layers)).getAs(key)!!

/** Returns the lesser of [a] and [b], [a] on a tie for stable sorting. */
private fun <T : Comparable<T>> min(a: T, b: T): T = if (b < a) b else a
