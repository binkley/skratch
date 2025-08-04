package hm.binkley.labs.skratch.layers.rules

import hm.binkley.labs.skratch.layers.Layers
import hm.binkley.labs.skratch.layers.ReversedSequence
import hm.binkley.labs.skratch.layers.Rule

class LastOrNullRule<
    K : Any,
    V : Any,
    T : V
> : Rule<K, V, T>("last-or[null]") {
    override fun invoke(
        key: K,
        values: ReversedSequence<T>,
        layers: Layers<K, V, *>
    ): T? = values.firstOrNull()
}
