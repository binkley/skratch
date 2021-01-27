package hm.binkley.labs.skratch

import java.util.AbstractMap.SimpleEntry

class SimpleLayers : Layers, AbstractMap<String, Any>() {
    // Sequence by newest to oldest
    private val top = mutableListOf<Map<String, Entry<Any>>>()
    override val entries: Set<Map.Entry<String, Any>>
        get() = top.flatMap {
            it.keys
        }.distinct().map {
            SimpleEntry(it, x(it, top))
        }.toSet()

    init {
        top.add(mapOf(
            "a" to object : Rule<String> {
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
            "a" to object : Rule<String> {
                override fun invoke(
                    key: String,
                    values: List<String>,
                    layers: Layers,
                ) = key.toUpperCase()

                override fun toString() = "UPCASE"
            },
            "b" to object : Rule<Int> {
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
    private fun x(key: String, top: List<Map<String, Entry<*>>>): Any {
        var rule: Rule<Any>? = null
        val values = ArrayList<Any>(top.size)

        for (layer in top) {
            val entry = layer[key] ?: continue
            when (entry) {
                is Rule<*> -> if (null == rule) rule = (entry as Rule<Any>)
                else -> values.add((entry as Value<Any>).value)
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

interface Entry<out T>

data class Value<out T>(
    val value: T,
) : Entry<T>

fun <T> T.ding() = Value(this)

interface Rule</* out */ T> : Entry<T>, (String, List<T>, Layers) -> T
