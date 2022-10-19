package hm.binkley.labs.skratch.layers.rules

import hm.binkley.labs.skratch.layers.Layers
import hm.binkley.labs.skratch.layers.ReversedSequence
import hm.binkley.labs.skratch.layers.Rule

class ConstantRule<
    K : Any,
    V : Any,
    T : V
    >(val constant: T) : Rule<K, V, T>("constant[$constant]") {
    override fun invoke(
        key: K,
        values: ReversedSequence<T>,
        layers: Layers<K, V, *>
    ): T = constant
}
