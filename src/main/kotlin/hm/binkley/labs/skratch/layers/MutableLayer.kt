package hm.binkley.labs.skratch.layers

abstract class MutableLayer<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    >(
    // TODO: Defensive copy of [map]
    private val map: MutableMap<K, ValueOrRule<V>>,
) : Layer<K, V, M>(map) {
    fun edit(block: EditMap<K, V>.() -> Unit): M {
        DefaultEditMap().block()
        return self
    }

    protected fun mapCopy() = map.toMutableMap()

    // TODO: How does subtypes of MutableLayer customize the edit map?
    private inner class DefaultEditMap :
        EditMap<K, V>, MutableMap<K, ValueOrRule<V>> by map
}
