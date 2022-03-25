package hm.binkley.labs.skratch.layers

sealed class ValueOrRule<V : Any>

data class Value<V : Any>(
    val value: V,
) : ValueOrRule<V>() {
    override fun toString() = "<Value>$value"
}

fun <T : Any> T.toValue() = Value(this)

typealias ReversedSequence<T> = Sequence<T>

abstract class Rule<
    K : Any,
    V : Any,
    T : V,
    >(
    val name: String,
) : ValueOrRule<V>(), (K, ReversedSequence<T>, Layers<K, T, *>) -> T? {
    override fun toString() = "<Rule>$name"
}
