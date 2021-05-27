package hm.binkley.labs.skratch.layers

import hm.binkley.labs.skratch.layers.B.Companion.newDefaultA
import hm.binkley.labs.skratch.layers.D.Companion.newDefaultC
import java.util.AbstractMap.SimpleEntry
import kotlin.collections.Map.Entry

fun main() {
    val c = newDefaultC<String, Number>()
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

interface A<K : Any, V : Any, L : A<K, V, L>> : Map<K, P<V>> {
    @Suppress("UNCHECKED_CAST")
    val self: L
        get() = this as L
}

interface MutableA<K : Any, V : Any, M : A<K, V, M>> :
    A<K, V, M>,
    MutableMap<K, P<V>> {

    fun edit(block: MutableMap<K, P<V>>.() -> Unit)
}

open class B<K : Any, V : Any, M : B<K, V, M>>(
    val map: MutableMap<K, P<V>> = mutableMapOf(),
) : MutableA<K, V, M>, MutableMap<K, P<V>> by map {
    override fun edit(block: MutableMap<K, P<V>>.() -> Unit) = map.block()

    override fun toString(): String = map.toString()

    companion object {
        fun <K : Any, V : Any> newDefaultA(): MutableA<K, V, *> {
            @Suppress("UNCHECKED_CAST")
            return B<K, V, B<K, V, *>>()
        }
    }
}

interface C<K : Any, V : Any, L : A<K, V, L>> : Map<K, V> {
    val history: List<Map<K, P<V>>>
}

interface MutableC<K : Any, V : Any, M : MutableA<K, V, M>> : C<K, V, M> {
    fun edit(block: MutableMap<K, P<V>>.() -> Unit)

    /** @todo How to return M instead? */
    fun commitAndNext(): MutableA<K, V, M>
}

open class D<K : Any, V : Any, M : MutableA<K, V, M>>(
    private val list: MutableList<M> = mutableListOf(),
    private val newA: () -> M,
) : MutableC<K, V, M>, AbstractMap<K, V>() {
    init {
        if (list.isEmpty()) list.add(newA())
    }

    companion object {
        fun <K : Any, V : Any> newDefaultC(): MutableC<K, V, *> =
            D<K, V, MutableA<K, V, *>> { newDefaultA<K, V>() }
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
