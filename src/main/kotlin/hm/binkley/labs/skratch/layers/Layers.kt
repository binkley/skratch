package hm.binkley.labs.skratch.layers

import hm.binkley.labs.skratch.layers.DefaultMutableLayer.Companion.defaultMutableLayer
import java.util.AbstractMap.SimpleEntry
import kotlin.collections.Map.Entry

interface Layers<K : Any, V : Any> : Map<K, V> {
    val name: String
    val history: List<Map<K, ValueOrRule<V>>>

    fun whatIf(
        name: String = "<WHAT-IF: ${this.name}>",
        scenario: Map<K, ValueOrRule<V>>,
    ): Map<K, V>
}

interface MutableLayers<K : Any, V : Any, M : MutableLayer<K, V, M>>
    : Layers<K, V> {
    fun edit(block: EditMap<K, V>.() -> Unit)

    /** @todo Returning M loses type information for K and V ?! */
    fun commitAndNext(): MutableLayer<K, V, M>
    fun <N : M> commitAndNext(nextMutableLayer: () -> N): N
}

open class DefaultMutableLayers<K : Any, V : Any, M : MutableLayer<K, V, M>>(
    override val name: String,
    private val layers: MutableList<M> = mutableListOf(),
    private val defaultMutableLayer: () -> M,
) : MutableLayers<K, V, M>, AbstractMap<K, V>() {
    init {
        if (layers.isEmpty()) layers.add(defaultMutableLayer())
    }

    companion object {
        fun <K : Any, V : Any> defaultMutableLayers(
            name: String,
        ): MutableLayers<K, V, *> =
            DefaultMutableLayers<K, V, MutableLayer<K, V, *>>(name) {
                defaultMutableLayer<K, V>()
            }
    }

    override val entries: Set<Entry<K, V>> get() = ViewSet()
    override val history: List<Map<K, ValueOrRule<V>>> = layers

    override fun whatIf(
        name: String,
        scenario: Map<K, ValueOrRule<V>>,
    ): Map<K, V> {
        val whatIfLayer = defaultMutableLayer()
        whatIfLayer.edit { putAll(scenario) }
        val whatIfLayers = layers.toMutableList()
        whatIfLayers.add(whatIfLayer)

        return DefaultMutableLayers(
            name,
            whatIfLayers,
            defaultMutableLayer,
        )
    }

    override fun edit(block: EditMap<K, V>.() -> Unit) =
        LayersEditMap().block()

    override fun commitAndNext(): M = commitAndNext(defaultMutableLayer)

    override fun <N : M> commitAndNext(nextMutableLayer: () -> N): N {
        val layer = nextMutableLayer()
        layers.add(layer)
        return layer
    }

    override fun toString() = history.mapIndexed { index, layer ->
        "$index: $layer"
    }.joinToString("\n", "$name: ${super.toString()}\n")

    private fun <T : V> currentRuleFor(key: K): Rule<K, V, T> =
        valuesOrRules(key).filterIsInstance<Rule<K, V, T>>().last()

    private fun <T : V> currentValuesFor(key: K): List<T> =
        valuesOrRules(key).filterIsInstance<Value<T>>().map { it.value }

    private fun valuesOrRules(key: K): List<ValueOrRule<V>> =
        layers.mapNotNull { it[key] }

    private inner class ViewIterator(keys: Set<K>) : Iterator<Entry<K, V>> {
        private val kit = keys.iterator()

        override fun hasNext(): Boolean = kit.hasNext()
        override fun next(): Entry<K, V> {
            val key = kit.next()
            return SimpleEntry(key, computeValue(key))
        }
    }

    private inner class ViewSet(val keys: Set<K> = allKeys()) :
        AbstractSet<Entry<K, V>>() {
        override val size: Int get() = keys.size
        override fun iterator(): Iterator<Entry<K, V>> = ViewIterator(keys)
    }

    private inner class ViewMap(private val except: K) : AbstractMap<K, V>() {
        override val entries: Set<Entry<K, V>>
            get() = ViewSet(allKeys().filterNot { it == except }.toSet())
    }

    private fun allKeys(): Set<K> = history.flatMap { it.keys }.toSet()
    private fun computeValue(key: K): V {
        val rule = currentRuleFor<V>(key)
        val values = currentValuesFor<V>(key)

        return rule(key, values, ViewMap(key))
    }

    private inner class LayersEditMap
        : EditMap<K, V>, MutableMap<K, ValueOrRule<V>> by layers.last() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : V> getOtherValueAs(key: K): T {
            return this@DefaultMutableLayers[key] as T
        }
    }
}
