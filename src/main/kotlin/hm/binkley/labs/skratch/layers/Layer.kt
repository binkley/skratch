package hm.binkley.labs.skratch.layers

interface Layer<
    K : Any,
    V : Any,
    L : Layer<K, V, L>,
    > : Map<K, ValueOrRule<V>> {
    @Suppress("UNCHECKED_CAST")
    val self: L get() = this as L
}
