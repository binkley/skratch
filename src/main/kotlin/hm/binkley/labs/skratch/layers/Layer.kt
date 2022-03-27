package hm.binkley.labs.skratch.layers

import kotlin.collections.Map.Entry

abstract class Layer<
    K : Any,
    V : Any,
    L : Layer<K, V, L>,
    >(
    // TODO: Defensive copy of [map]
    //       Pointer sharing with MutableLayer init makes this hard to do
    private val map: Map<K, ValueOrRule<V>>,
) : AbstractMap<K, ValueOrRule<V>>() {
    // TODO: Override rather than delegate to avoid a recursion loop
    override val entries: Set<Entry<K, ValueOrRule<V>>>
        get() = map.entries

    override fun toString() = map.toString()

    @Suppress("UNCHECKED_CAST")
    val self: L get() = this as L

    // TODO: Factory pattern instead?
    abstract fun <N : L> duplicate(): N
}
