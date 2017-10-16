package hm.binkley.labs.skratch.knapsack

import java.util.Objects

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

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as DatabaseValue

            return database == other.database && value == other.value
        }

        override fun hashCode() = Objects.hash(database, value)
    }

    class RuleValue<out T>(val rule: Rule<T>) : Value() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as RuleValue<*>

            return rule == other.rule
        }

        override fun hashCode() = Objects.hash(rule)
    }
}
