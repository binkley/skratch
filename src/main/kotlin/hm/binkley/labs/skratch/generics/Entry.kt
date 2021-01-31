package hm.binkley.labs.skratch.generics

sealed class Entry<T> {
    abstract override fun toString(): String
}

data class Value<T>(
    val value: T,
) : Entry<T>() {
    override fun toString() = "<Value>: $value"
}

abstract class Rule<T>(
    protected val key: String,
) : Entry<T>() {
    abstract operator fun invoke(
        values: List<T>,
        allValues: Map<String, Any>,
    ): T

    abstract fun description(): String

    final override fun toString() = "<Rule>[$key]: ${description()}"
}

fun <T> Rule<T>.defaultValue() = this(emptyList(), emptyMap())
fun <T> T.toValue(): Entry<T> = Value(this)

fun <T> ruleFor(
    key: String,
    block: (List<T>, Map<String, Any>) -> T,
) = object : Rule<T>(key) {
    override fun invoke(
        values: List<T>,
        allValues: Map<String, Any>,
    ) = block(values, allValues)

    override fun description() = "<Anonymous>"
}
