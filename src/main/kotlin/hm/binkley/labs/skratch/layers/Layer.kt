package hm.binkley.labs.skratch.layers

interface Layer<
    K : Any,
    out V : Any,
    out L : Layer<K, V, L>,
    > : Map<K, V> {
    @Suppress("UNCHECKED_CAST")
    val self: L get() = this as L
}

abstract class AbstractLayer<
    K : Any,
    out V : Any,
    out L : AbstractLayer<K, V, L>,
    >(
    private val map: Map<K, V>,
) : Layer<K, V, L>, Map<K, V> by map {
    override fun toString() = map.toString()
}
