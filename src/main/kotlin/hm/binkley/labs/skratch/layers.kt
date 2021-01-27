package hm.binkley.labs.skratch

import java.util.AbstractMap.SimpleEntry
import kotlin.collections.Map.Entry

class SimpleLayers : Layers, AbstractMap<String, Any>() {
    // Sequence by newest to oldest
    private val top = mutableListOf<Map<String, Ding<Any>>>()
    override val entries: Set<Entry<String, Any>>
        get() = top.flatMap {
            it.keys
        }.distinct().map {
            SimpleEntry(it, x(it, top))
        }.toSet()

    init {
        top.add(mapOf(
            "a" to object : RuleDing<String> {
                override fun invoke(
                    key: String,
                    values: List<String>,
                    layers: Layers,
                ) = key.toLowerCase()

                override fun toString() = "DOWNCASE"
            },
        ))
        top.add(mapOf(
            "b" to 4.ding(),
        ))
        top.add(mapOf(
            "a" to "A".ding(),
            "b" to 3.ding(),
        ))
        top.add(mapOf(
            "a" to object : RuleDing<String> {
                override fun invoke(
                    key: String,
                    values: List<String>,
                    layers: Layers,
                ) = key.toUpperCase()

                override fun toString() = "UPCASE"
            },
            "b" to object : RuleDing<Int> {
                override fun invoke(
                    key: String,
                    values: List<Int>,
                    layers: Layers,
                ) = 2 * values.first()

                override fun toString() = "DOUBLE MOST RECENT"
            }
        ))
    }

    @Suppress("UNCHECKED_CAST")
    private fun x(key: String, top: List<Map<String, Ding<*>>>): Any {
        var rule: RuleDing<Any>? = null
        val values = ArrayList<Any>(top.size)

        for (layer in top) {
            val ding = layer[key] ?: continue
            when (ding) {
                is RuleDing<*> -> if (null == rule) rule =
                    (ding as RuleDing<Any>)
                else -> values.add((ding as ValueDing<Any>).value)
            }
        }

        if (null == rule) error("No rule for key: $key")

        return rule(key, values, this)
    }
}

fun main() {
    val layer = SimpleLayers()
    println("LAYER -> $layer")
    println("c -> ${layer["c"]}")
}

// TODO: How many times am I going to play with this?

interface Layer : Map<String, Any>
interface Layers : Map<String, Any>

interface Ding<out T>

data class ValueDing<out T>(
    val value: T,
) : Ding<T>

fun <T> T.ding() = ValueDing(this)

interface RuleDing</* out */ T> : Ding<T>,
        (String, List<T>, Layers) -> T
