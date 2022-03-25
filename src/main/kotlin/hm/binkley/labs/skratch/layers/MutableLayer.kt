package hm.binkley.labs.skratch.layers

import hm.binkley.labs.skratch.layers.rules.LastOrDefaultRule
import hm.binkley.labs.skratch.layers.rules.LastOrNullRule
import hm.binkley.labs.skratch.layers.rules.LastRule

interface EditMap<K : Any, V : Any> : MutableMap<K, ValueOrRule<V>> {
    fun <T : V> lastRule() = LastRule<K, V, T>()
    fun <T : V> lastOrDefaultRule(default: T) =
        LastOrDefaultRule<K, V, T>(default)

    fun <T : V> lastOrNullRule() = LastOrNullRule<K, V, T>()
}

abstract class MutableLayer<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    >(
    private val map: MutableMap<K, ValueOrRule<V>>,
) : Layer<K, V, M>(map) {
    fun edit(block: EditMap<K, V>.() -> Unit): M {
        DefaultEditMap().block()
        return self
    }

    private inner class DefaultEditMap :
        EditMap<K, V>, MutableMap<K, ValueOrRule<V>> by map
}
