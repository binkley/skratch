package hm.binkley.labs.skratch.layers

import kotlin.collections.Map.Entry

abstract class MutableLayer<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    >(
    // TODO: Defensive copy of [map]
    private val map: MutableMap<K, ValueOrRule<V>>,
) : Layer<K, V, M>, AbstractMap<K, ValueOrRule<V>>() {
    // TODO: Override rather than delegate to avoid a recursion loop
    override val entries: Set<Entry<K, ValueOrRule<V>>>
        get() = map.entries

    override fun toString() = map.toString()

    // TODO: Factory function instead?
    abstract fun <N : M> duplicate(): N

    fun edit(block: EditMap<K, V>.() -> Unit): M {
        DefaultEditMap().block()
        return self
    }

    // TODO: How does subtypes of MutableLayer customize the edit map?
    private inner class DefaultEditMap :
        EditMap<K, V>, MutableMap<K, ValueOrRule<V>> by map
}
