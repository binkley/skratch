package hm.binkley.labs.skratch.layers

import hm.binkley.util.Stack
import org.jetbrains.kotlin.utils.addToStdlib.firstIsInstanceOrNull
import java.util.AbstractMap.SimpleImmutableEntry
import kotlin.collections.Map.Entry

abstract class Layers<
    K : Any,
    V : Any,
    L : Layer<K, V, L>,
    >(
    // TODO: Defensive copy of [layers]
    // TODO: Avoid pointer sharing with [MutableLayers]
    // TODO: Relax type to `List<L>`
    private val layers: Stack<L>,
) : AbstractMap<K, V>() {
    override val entries: Set<Entry<K, V>> get() = RuledEntries()

    val history: List<L> get() = layers

    fun <T : V> getAs(key: K): T? = valueFor(key)
    fun peek(): L = layers.peek()

    private fun keys(): Set<K> =
        layers.asSequence().flatMap {
            it.keys
        }.toSet()

    private fun <T : V> valueFor(key: K): T? {
        val rule = ruleForOrThrow<T>(key)
        val values = valuesFor<T>(key)

        return rule(key, values, this)
    }

    // TODO: Merge [Layers] and [MutableLayers]: avoid `protected`
    protected fun <T : V> ruleForOrThrow(key: K): Rule<K, V, T> =
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

    private inner class RuledEntries : AbstractSet<Entry<K, V>>() {
        override val size: Int get() = keys().mapNotNull { valueFor(it) }.size

        override fun iterator(): Iterator<Entry<K, V>> =
            keys().asSequence().map {
                SimpleImmutableEntry<K, V>(it, valueFor(it))
            }.filter {
                null != it.value // Let rules hide entries
            }.iterator()
    }
}
