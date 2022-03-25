package hm.binkley.labs.skratch.layers.rules

import hm.binkley.labs.skratch.layers.Layers
import hm.binkley.labs.skratch.layers.MissingValuesException
import hm.binkley.labs.skratch.layers.Rule

class LastRule<
    K : Any,
    V : Any,
    T : V,
    > : Rule<K, V, T>("last") {
    override fun invoke(
        key: K,
        values: Sequence<T>,
        layers: Layers<K, T, *>,
    ): T = values.firstOrNull() ?: throw MissingValuesException(key)
}
