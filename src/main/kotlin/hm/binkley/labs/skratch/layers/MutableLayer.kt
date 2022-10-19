package hm.binkley.labs.skratch.layers

interface MutableLayer<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>
    > : Layer<K, V, M> {
    /** Safe alternative to [Object.clone] used for validation and what-if. */
    fun copy(): M

    fun edit(block: EditMap<K, V>.() -> Unit): M
}
