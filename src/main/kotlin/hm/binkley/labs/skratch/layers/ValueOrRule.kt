package hm.binkley.labs.skratch.layers

sealed class ValueOrRule<out V : Any>

data class Value<out V : Any>(
    val value: V,
) : ValueOrRule<V>() {
    override fun toString() = "<Value>$value"
}

fun <T : Any> T.toValue() = Value(this)

abstract class AbstractRule<
    K : Any,
    out V : Any,
    T : V,
    >(
    val name: String,
) : ValueOrRule<V>(), (K, List<T>, Layers<K, T, *>) -> T {
    override fun toString() = "<Rule>$name"
}
