package hm.binkley.labs.skratch.collections

typealias Rule<T> = (String, Layers) -> T

class Layers

sealed class Value {
    open fun remove(layer: Int, key: String) = Unit
    open fun replaceWith(layer: Int, key: String, other: Value) = Unit
    open fun add(layer: Int, key: String) = Unit

    object Nonce : Value() {
        override fun remove(layer: Int, key: String)
                = throw IllegalStateException()

        override fun replaceWith(layer: Int, key: String, other: Value)
                = throw IllegalStateException()

        override fun add(layer: Int, key: String)
                = throw IllegalStateException()
    }

    class DatabaseValue(private val database: Database, val value: String)
        : Value() {
        override fun remove(layer: Int, key: String)
                = database.remove(layer, key)

        override fun replaceWith(layer: Int, key: String, other: Value) {
            when (other) {
                Nonce -> remove(layer, key)
                is RuleValue<*> -> remove(layer, key)
                is DatabaseValue -> Unit
            }
        }

        override fun add(layer: Int, key: String)
                = database.upsert(layer, key, value)
    }

    class RuleValue<out T>(val rule: Rule<T>) : Value()
}

