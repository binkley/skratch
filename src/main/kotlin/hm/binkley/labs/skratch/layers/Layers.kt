package hm.binkley.labs.skratch.layers

import org.jetbrains.kotlin.utils.addToStdlib.firstIsInstanceOrNull
import java.util.AbstractMap.SimpleImmutableEntry
import kotlin.collections.Map.Entry

interface Layers<
    K : Any,
    V : Any,
    L : Layer<K, V, L>,
    > : Map<K, V> {
    val history: List<L>

    @Suppress("UNCHECKED_CAST")
    fun <T : V> getAs(key: K): T? = this[key] as T

    fun last(): L = history.last()
}

abstract class AbstractLayers<
    K : Any,
    V : Any,
    L : Layer<K, V, L>,
    >(
    private val layers: List<L>,
) : AbstractMap<K, V>(), Layers<K, V, L> {
    override val entries: Set<Entry<K, V>> get() = RuledView().entries
    override val history: List<L> get() = layers
    override fun <T : V> getAs(key: K): T? = valueFor(key)

    private fun keys(): Set<K> =
        layers.asSequence().flatMap {
            it.keys
        }.toSet()

    @Suppress("UNCHECKED_CAST")
    private fun <T : V> valueFor(key: K): T? {
        val rule = ruleFor<T>(key)
        val values = valuesFor<T>(key)
        val layers = this as Layers<K, T, *>

        return rule(key, values, layers)
    }

    private fun <T : V> ruleFor(key: K): Rule<K, V, T> =
        valuesOrRulesFor<T>(key).firstIsInstanceOrNull()
            ?: throw MissingRuleException(key)

    private fun <T : V> valuesFor(key: K): Sequence<T> =
        valuesOrRulesFor<T>(key).filterIsInstance<Value<T>>().map {
            it.value
        }

    private fun <T : V> valuesOrRulesFor(key: K): Sequence<ValueOrRule<T>> =
        layers.asReversed().asSequence().map {
            it[key]
        }.filterIsInstance<ValueOrRule<T>>()

    private inner class RuledView : AbstractMap<K, V>() {
        override val entries: Set<Entry<K, V>> get() = Entries()

        private inner class Entries : AbstractSet<Entry<K, V>>() {
            override val size: Int
                get() = keys().size

            override fun iterator(): Iterator<Entry<K, V>> =
                keys().asSequence().map {
                    SimpleImmutableEntry<K, V>(it, valueFor(it))
                }.filter {
                    null != it.value
                }.iterator()
        }
    }
}
