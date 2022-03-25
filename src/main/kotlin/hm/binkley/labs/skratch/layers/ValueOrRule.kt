package hm.binkley.labs.skratch.layers

sealed class ValueOrRule<V : Any>

data class Value<V : Any>(
    val value: V,
) : ValueOrRule<V>() {
    override fun toString() = "<Value>$value"
}

fun <T : Any> T.toValue() = Value(this)

abstract class Rule<
    K : Any,
    V : Any,
    T : V,
    >(
    val name: String,
) : ValueOrRule<V>(), (K, Sequence<T>, Layers<K, T, *>) -> T? {
    override fun toString() = "<Rule>$name"
}
