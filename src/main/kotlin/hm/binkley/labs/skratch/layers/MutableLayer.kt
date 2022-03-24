package hm.binkley.labs.skratch.layers

interface MutableLayer<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    > :
    Layer<K, V, M> {
    fun edit(block: EditMap<K, V>.() -> Unit): M
}

interface EditMap<K : Any, V : Any> : MutableMap<K, V>

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
