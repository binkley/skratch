package hm.binkley.labs.skratch.layers

import kotlin.collections.Map.Entry

abstract class MutableLayer<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    >(
    override val index: Int,
    map: Map<K, ValueOrRule<V>>,
) : Layer<K, V, M>, AbstractMap<K, ValueOrRule<V>>() {
    private val map = map.toMutableMap() // Defensive copy

    override val entries: Set<Entry<K, ValueOrRule<V>>> get() = map.entries

    override fun toString() = map.toString()

    /** Safe alternative to [Object.clone] used for validation and what-if. */
    abstract fun copy(): M

    fun edit(block: EditMap<K, V>.() -> Unit): M {
        DefaultEditMap().block()
        return self()
    }

    private inner class DefaultEditMap :
        EditMap<K, V>, MutableMap<K, ValueOrRule<V>> by map
}
