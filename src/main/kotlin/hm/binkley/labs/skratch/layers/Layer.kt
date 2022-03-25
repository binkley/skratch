package hm.binkley.labs.skratch.layers

import kotlin.collections.Map.Entry

interface Layer<
    K : Any,
    out V : Any,
    out L : Layer<K, V, L>,
    > : Map<K, ValueOrRule<V>> {
    @Suppress("UNCHECKED_CAST")
    val self: L get() = this as L
}

abstract class AbstractLayer<
    K : Any,
    out V : Any,
    out L : Layer<K, V, L>,
    >(
    private val map: Map<K, ValueOrRule<V>>,
) : AbstractMap<K, ValueOrRule<V>>(), Layer<K, V, L> {
    // Override rather than delegate to avoid a recursion loop
    override val entries: Set<Entry<K, ValueOrRule<V>>>
        get() = map.entries

    override fun toString() = map.toString()
}
