package hm.binkley.labs.skratch.layers

import kotlin.collections.Map.Entry

abstract class MutableLayer<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    >(
    map: Map<K, ValueOrRule<V>>,
) : Layer<K, V, M>, AbstractMap<K, ValueOrRule<V>>() {
    private val map = map.toMutableMap() // Defensive copy

    // TODO: Override rather than delegate
    //       Delegates for derived types to manipulate in init block
    // Specify type to restrict mutation to [edit]
    override val entries: Set<Entry<K, ValueOrRule<V>>> get() = map.entries

    override fun toString() = map.toString()

    // TODO: Factory function instead?
    abstract fun <N : M> copy(): N

    fun edit(block: EditMap<K, V>.() -> Unit): M {
        DefaultEditMap().block()
        return self()
    }

    // TODO: How does subtypes of MutableLayer customize the edit map?
    private inner class DefaultEditMap :
        EditMap<K, V>, MutableMap<K, ValueOrRule<V>> by map
}
