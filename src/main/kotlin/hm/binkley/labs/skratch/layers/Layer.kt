package hm.binkley.labs.skratch.layers

interface Layer<
    K : Any,
    V : Any,
    L : Layer<K, V, L>
> : Map<K, ValueOrRule<V>> {
    /** Position within [Layers]. */
    val index: Int
}

/** Convenience for caller avoiding type cast warning. */
@Suppress("UNCHECKED_CAST")
fun <
    K : Any,
    V : Any,
    L : Layer<K, V, L>,
    N : L
> Layer<K, V, L>.self(): N = this as N
