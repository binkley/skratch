package hm.binkley.labs.skratch.layers

import hm.binkley.labs.skratch.layers.DefaultMutableLayer.Companion.defaultMutableLayer
import hm.binkley.labs.skratch.layers.DefaultMutableLayers.Companion.defaultMutableLayers
import java.util.AbstractMap.SimpleEntry
import kotlin.collections.Map.Entry

fun main() {
    val c = defaultMutableLayers<String, Number>()
    c.edit {
        this["ALICE"] =
            Rule.rule<String, Number, Int>("Latest of") { _, values, _ ->
                if (values.isEmpty()) 0 else values.last()
            }
        this["BOB"] =
            Rule.rule<String, Number, Double>("Sum[Int]") { _, values, _ ->
                values.sum()
            }
    }
    c.commitAndNext()
    c.edit {
        this["ALICE"] = Value(3)
    }
    val a = c.commitAndNext()
    a.edit {
        this["BOB"] = Value(4.0)
    }
    println(c.history)
    println(c)
}

sealed interface ValueOrRule<V : Any>
data class Value<V : Any>(val value: V) : ValueOrRule<V> {
    override fun toString() = "<Value>:$value"
}

abstract class Rule<K : Any, V : Any, T : V>(
    val name: String,
) : ValueOrRule<V>,
        (K, List<T>, Layers<K, V, *>) -> T {
    override fun toString() = "<Rule>:$name"

    companion object {
        fun <K : Any, V : Any, T : V> rule(
            name: String,
            block: (K, List<T>, Layers<K, V, *>) -> T,
        ): Rule<K, V, T> = object : Rule<K, V, T>(name) {
            override fun invoke(
                key: K,
                values: List<T>,
                layers: Layers<K, V, *>,
            ): T = block(key, values, layers)
        }
    }
}

interface Layer<K : Any, V : Any, L : Layer<K, V, L>> :
    Map<K, ValueOrRule<V>> {
    @Suppress("UNCHECKED_CAST")
    val self: L
        get() = this as L
}

interface MutableLayer<K : Any, V : Any, M : MutableLayer<K, V, M>> :
    Layer<K, V, M>,
    MutableMap<K, ValueOrRule<V>> {
    fun edit(block: MutableMap<K, ValueOrRule<V>>.() -> Unit)
}

open class DefaultMutableLayer<K : Any, V : Any, M : DefaultMutableLayer<K, V, M>>(
    val map: MutableMap<K, ValueOrRule<V>> = mutableMapOf(),
) : MutableLayer<K, V, M>, MutableMap<K, ValueOrRule<V>> by map {
    override fun edit(block: MutableMap<K, ValueOrRule<V>>.() -> Unit) =
        map.block()

    override fun toString(): String = map.toString()

    companion object {
        fun <K : Any, V : Any> defaultMutableLayer(): MutableLayer<K, V, *> {
            @Suppress("UNCHECKED_CAST")
            return DefaultMutableLayer<K, V, DefaultMutableLayer<K, V, *>>()
        }
    }
}

interface Layers<K : Any, V : Any, L : Layer<K, V, L>> : Map<K, V> {
    val history: List<Map<K, ValueOrRule<V>>>
}

interface MutableLayers<K : Any, V : Any, M : MutableLayer<K, V, M>>
    : Layers<K, V, M> {
    fun edit(block: MutableMap<K, ValueOrRule<V>>.() -> Unit)

    fun commitAndNext(): MutableLayer<K, V, M>
    fun <N : M> commitAndNext(next: () -> N): N
}

open class DefaultMutableLayers<K : Any, V : Any, M : MutableLayer<K, V, M>>(
    private val layers: MutableList<M> = mutableListOf(),
    private val defaultMutableLayer: () -> M,
) : MutableLayers<K, V, M>, AbstractMap<K, V>() {
    init {
        if (layers.isEmpty()) layers.add(defaultMutableLayer())
    }

    companion object {
        fun <K : Any, V : Any> defaultMutableLayers(): MutableLayers<K, V, *> =
            DefaultMutableLayers<K, V, MutableLayer<K, V, *>> {
                defaultMutableLayer<K, V>()
            }
    }

    override val history: List<Map<K, ValueOrRule<V>>> = layers

    override fun edit(block: MutableMap<K, ValueOrRule<V>>.() -> Unit) =
        layers.last().block()

    override fun commitAndNext(): M = commitAndNext(defaultMutableLayer)

    override fun <N : M> commitAndNext(next: () -> N): N {
        val layer = next()
        layers.add(layer)
        return layer
    }

    override val entries: Set<Entry<K, V>>
        get() = object : AbstractSet<Entry<K, V>>() {
            override val size: Int get() = allKeys().size

            override fun iterator(): Iterator<Entry<K, V>> =
                object : Iterator<Entry<K, V>> {
                    val kit = allKeys().iterator()
                    override fun hasNext(): Boolean = kit.hasNext()

                    override fun next(): Entry<K, V> {
                        val key = kit.next()
                        return SimpleEntry(key, computeValue(key))
                    }
                }
        }

    private fun allKeys(): Set<K> = history.flatMap { it.keys }.toSet()
    private fun computeValue(key: K): V {
        val rule = currentRuleFor<V>(key)
        val values = currentValuesFor<V>(key)

        return rule(key, values, this)
    }

    private fun <T : V> currentRuleFor(key: K): Rule<K, V, T> =
        valuesOrRules(key).filterIsInstance<Rule<K, V, T>>().last()

    private fun <T : V> currentValuesFor(key: K): List<T> =
        valuesOrRules(key).filterIsInstance<Value<T>>().map { it.value }

    private fun valuesOrRules(key: K): List<ValueOrRule<V>> =
        layers.mapNotNull { it[key] }
}
