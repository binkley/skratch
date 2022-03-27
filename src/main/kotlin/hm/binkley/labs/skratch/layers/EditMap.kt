package hm.binkley.labs.skratch.layers

interface EditMap<K : Any, V : Any> : MutableMap<K, ValueOrRule<V>> {
    /** Convenience to put values directly. */
    fun put(key: K, value: V) = put(key, value.toValue())
    operator fun <T : V> set(key: K, value: T) = put(key, value)

    fun <T : V> rule(
        name: String,
        block: (K, Sequence<T>, Layers<K, V, *>) -> T?,
    ): Rule<K, V, T> = object : Rule<K, V, T>(name) {
        override fun invoke(
            key: K,
            values: ReversedSequence<T>,
            layers: Layers<K, V, *>,
        ): T? = block(key, values, layers)
    }
}
