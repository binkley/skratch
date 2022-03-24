package hm.binkley.labs.skratch.layers

import kotlin.collections.Map.Entry

interface Layers<
    K : Any,
    out V : Any,
    out L : Layer<K, V, L>,
    > :
    Map<K, V> {
    val history: List<L>

    fun last(): L = history.last()
}

abstract class AbstractLayers<
    K : Any,
    out V : Any,
    out L : Layer<K, V, L>,
    >(
    private val layers: List<L>,
) : Layers<K, V, L>, Map<K, V> by DefaultView(layers) {
    override val history: List<L> get() = layers

    override fun toString() = DefaultView(layers).toString()
}

private class DefaultView<
    K : Any,
    out V : Any,
    out L : Layer<K, V, L>,
    >(
    private val layers: List<L>,
) : AbstractMap<K, V>() {
    override val entries: Set<Entry<K, V>>
        get() = layers.fold<L, MutableMap<K, V>>(mutableMapOf()) { merged, it ->
            merged.putAll(it); merged
        }.entries
}
