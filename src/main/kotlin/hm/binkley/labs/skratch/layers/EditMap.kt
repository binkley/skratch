package hm.binkley.labs.skratch.layers

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface EditMap<K : Any, V : Any> : MutableMap<K, ValueOrRule<V>> {

    /** Convenience for converting [value] to a [Value]. */
    fun put(key: K, value: V) = put(key, value.toValue())

    /** Creates an untyped rule. */
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

/** Convenience for converting [value] to a [Value]. */
operator fun <
    K : Any,
    V : Any,
    T : V>
EditMap<K, V>.set(key: K, value: T) = put(key, value)

fun interface EditMapDelegate<
    K : Any,
    V : Any,
    T : V,
    > : ReadWriteProperty<EditMap<K, V>, T?> {
    /** Finds or creates a suitable edit map key. */
    fun KProperty<*>.toKey(): K

    @Suppress("UNCHECKED_CAST")
    override operator fun getValue(
        thisRef: EditMap<K, V>,
        property: KProperty<*>
    ): T {
        val key = property.toKey()
        return when (val it = thisRef[key]) {
            null -> throw MissingKeyException(key)
            is Rule<*, *, *> -> throw NotAValueException(key)
            else -> (it as Value<T>).value
        }
    }

    override operator fun setValue(
        thisRef: EditMap<K, V>,
        property: KProperty<*>,
        value: T?
    ) {
        if (null == value) thisRef.remove(property.toKey())
        else thisRef[property.toKey()] = value.toValue()
    }
}
