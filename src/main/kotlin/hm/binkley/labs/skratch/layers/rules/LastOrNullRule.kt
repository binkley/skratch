package hm.binkley.labs.skratch.layers.rules

import hm.binkley.labs.skratch.layers.Layers
import hm.binkley.labs.skratch.layers.MissingRuleException
import hm.binkley.labs.skratch.layers.MissingValuesException
import hm.binkley.labs.skratch.layers.Rule

class LastOrNullRule<
    K : Any,
    V : Any,
    T : V,
    > : Rule<K, V, T>("last-or[null]") {
    override fun invoke(
        key: K,
        values: Sequence<T>,
        layers: Layers<K, T, *>,
    ): T? = values.firstOrNull()
}
