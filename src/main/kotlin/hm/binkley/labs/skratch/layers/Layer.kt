package hm.binkley.labs.skratch.layers

interface Layer<
    K : Any,
    V : Any,
    L : Layer<K, V, L>,
    > : Map<K, ValueOrRule<V>> {
    @Suppress("UNCHECKED_CAST")
    fun <N: L> self(): N = this as N
}
