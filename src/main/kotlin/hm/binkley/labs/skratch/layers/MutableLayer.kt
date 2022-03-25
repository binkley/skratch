package hm.binkley.labs.skratch.layers

import hm.binkley.labs.skratch.layers.rules.LastOrDefaultRule
import hm.binkley.labs.skratch.layers.rules.LastOrNullRule
import hm.binkley.labs.skratch.layers.rules.LastRule

abstract class MutableLayer<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    >(
    // TODO: Defensive copy of [map]
    private val map: MutableMap<K, ValueOrRule<V>>,
) : Layer<K, V, M>(map) {
    fun edit(block: EditMap<K, V>.() -> Unit): M {
        DefaultEditMap().block()
        return self
    }

    // TODO: How does subtypes of MutableLayer customize the edit map?
    private inner class DefaultEditMap :
        EditMap<K, V>, MutableMap<K, ValueOrRule<V>> by map
}

interface EditMap<K : Any, V : Any> : MutableMap<K, ValueOrRule<V>> {
    /** Convenience to put values directly. */
    fun put(key: K, value: V) {
        this[key] = value.toValue()
    }

    operator fun <T : V> set(key: K, value: T) = put(key, value)

    fun <T : V> lastRule() = LastRule<K, V, T>()
    fun <T : V> lastOrDefaultRule(default: T) =
        LastOrDefaultRule<K, V, T>(default)

    fun <T : V> lastOrNullRule() = LastOrNullRule<K, V, T>()

    fun <T : V> rule(
        name: String,
        block: (K, Sequence<T>, Layers<K, V, *>) -> T?,
    ): Rule<K, V, T> = object : Rule<K, V, T>(name) {
        override fun invoke(
            key: K,
            values: ReversedSequence<T>,
            layers: Layers<K, V, *>,
        ): T? = block(key, values, layers)
    }
}
