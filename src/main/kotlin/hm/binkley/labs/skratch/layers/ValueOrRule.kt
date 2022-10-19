package hm.binkley.labs.skratch.layers

sealed class ValueOrRule<V : Any>

/**
 * A wrapper for the value of a key in a [Layers].
 *
 * @param value the value
 */
data class Value<V : Any>(
    val value: V
) : ValueOrRule<V>() {
    override fun toString() = "<Value>$value"
}

/** Convenience for converting a value to a [Value]. */
fun <T : Any> T.toValue() = Value(this)

/** Marker alias to raise readability. */
typealias ReversedSequence<T> = Sequence<T>

/**
 * The base type for rules in computing the present value of a key in a
 * [Layers], taking:
 * - the key of type [K]
 * - the values of the key in newest-to-oldest order
 * - the layers
 *
 * @param name the rule name
 */
abstract class Rule<
    K : Any,
    V : Any,
    T : V
    >(
    val name: String
) : ValueOrRule<V>(), (K, ReversedSequence<T>, Layers<K, V, *>) -> T? {
    override fun toString() = "<Rule>$name"
}
