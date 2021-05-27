package hm.binkley.labs.skratch.layers

import hm.binkley.labs.skratch.layers.DefaultMutableLayer.Companion.defaultMutableLayer
import hm.binkley.labs.skratch.layers.DefaultMutableLayers.Companion.defaultMutableLayers
import java.util.AbstractMap.SimpleEntry
import kotlin.collections.Map.Entry

fun main() {
    val c = defaultMutableLayers<String, Number>()
    c.edit {
        this["ALICE"] = P(3)
    }
    val a = c.commitAndNext()
    a.edit {
        this["BOB"] = P(4.0)
    }
    println(c)
    println(c.history)
}

data class P<V : Any>(val value: V)

interface Layer<K : Any, V : Any, L : Layer<K, V, L>> : Map<K, P<V>> {
    @Suppress("UNCHECKED_CAST")
    val self: L
        get() = this as L
}

interface MutableLayer<K : Any, V : Any, M : Layer<K, V, M>> :
    Layer<K, V, M>,
    MutableMap<K, P<V>> {

    fun edit(block: MutableMap<K, P<V>>.() -> Unit)
}

open class DefaultMutableLayer<K : Any, V : Any, M : DefaultMutableLayer<K, V, M>>(
    val map: MutableMap<K, P<V>> = mutableMapOf(),
) : MutableLayer<K, V, M>, MutableMap<K, P<V>> by map {
    override fun edit(block: MutableMap<K, P<V>>.() -> Unit) = map.block()

    override fun toString(): String = map.toString()

    companion object {
        fun <K : Any, V : Any> defaultMutableLayer(): MutableLayer<K, V, *> {
            @Suppress("UNCHECKED_CAST")
            return DefaultMutableLayer<K, V, DefaultMutableLayer<K, V, *>>()
        }
    }
}

interface Layers<K : Any, V : Any, L : Layer<K, V, L>> : Map<K, V> {
    val history: List<Map<K, P<V>>>
}

interface MutableLayers<K : Any, V : Any, M : MutableLayer<K, V, M>>
    : Layers<K, V, M> {
    fun edit(block: MutableMap<K, P<V>>.() -> Unit)

    /** @todo How to return M instead? */
    fun commitAndNext(): MutableLayer<K, V, M>
}

open class DefaultMutableLayers<K : Any, V : Any, M : MutableLayer<K, V, M>>(
    private val list: MutableList<M> = mutableListOf(),
    private val newA: () -> M,
) : MutableLayers<K, V, M>, AbstractMap<K, V>() {
    init {
        if (list.isEmpty()) list.add(newA())
    }

    companion object {
        fun <K : Any, V : Any> defaultMutableLayers(): MutableLayers<K, V, *> =
            DefaultMutableLayers<K, V, MutableLayer<K, V, *>> {
                defaultMutableLayer<K, V>()
            }
    }

    override val history: List<Map<K, P<V>>> = list

    override fun edit(block: MutableMap<K, P<V>>.() -> Unit) =
        list.last().block()

    override fun commitAndNext(): M {
        val a = newA()
        list.add(a)
        return a
    }

    private fun keys(): Set<K> = history.flatMap { it.keys }.toSet()
    private fun computeValue(key: K): V =
        history.first { it.containsKey(key) }[key]!!.value

    override val entries: Set<Entry<K, V>>
        get() = object : AbstractSet<Entry<K, V>>() {
            override val size: Int get() = keys().size

            override fun iterator(): Iterator<Entry<K, V>> =
                object : Iterator<Entry<K, V>> {
                    val kit = keys().iterator()
                    override fun hasNext(): Boolean = kit.hasNext()

                    override fun next(): Entry<K, V> {
                        val key = kit.next()
                        return SimpleEntry(key, computeValue(key))
                    }
                }
        }
}
