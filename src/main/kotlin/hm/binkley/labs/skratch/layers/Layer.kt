package hm.binkley.labs.skratch.layers

interface Layer<
    K : Any,
    out V : Any,
    out L : Layer<K, V, L>,
    > : Map<K, V> {
    @Suppress("UNCHECKED_CAST")
    val self: L get() = this as L
}

interface EditMap<K : Any, V : Any> : MutableMap<K, V>

interface MutableLayer<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    > :
    Layer<K, V, M> {
    fun edit(block: EditMap<K, V>.() -> Unit): M
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

abstract class AbstractMutableLayer<
    K : Any,
    V : Any,
    M : AbstractMutableLayer<K, V, M>,
    >(
    private val map: MutableMap<K, V>,
) : MutableLayer<K, V, M>, AbstractLayer<K, V, M>(map) {
    override fun edit(block: EditMap<K, V>.() -> Unit): M {
        DefaultEditMap().block()
        return self
    }

    private inner class DefaultEditMap :
        EditMap<K, V>, MutableMap<K, V> by map
}
