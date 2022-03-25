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
) : Layers<K, V, L>, Map<K, V> by LastView(layers) {
    override val history: List<L> get() = layers

    override fun toString() = LastView(layers).toString()
}

private class LastView<
    K : Any,
    out V : Any,
    out L : Layer<K, V, L>,
    >(
    private val layers: List<L>,
) : AbstractMap<K, V>() {
    override val entries: Set<Entry<K, V>>
        get() = layers.fold<L, MutableMap<K, V>>(mutableMapOf()) { merged, it ->
            val unwrapped = it.mapValues { (_, value) ->
                when (value) {
                    is Value<V> -> value.value
                    else -> TODO("IMPLEMENT RULES")
                }
            }
            merged.putAll(unwrapped)
            merged
        }.entries
}
