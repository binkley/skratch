package hm.binkley.labs.skratch.delegation

import java.util.AbstractMap.SimpleEntry
import java.util.function.BiConsumer
import kotlin.collections.Map.Entry

class Getter(private val value: Any) {
    @Suppress("UNCHECKED_CAST")
    fun <T> getAs() = value as T

    fun getRaw() = value
}

class TypedMap(private val map: HashMap<Any, Any> = HashMap())
    : Map<Any, Getter> {
    override val entries: Set<Entry<Any, Getter>>
        get() = TypedEntries(map.entries)
    override val values: Collection<Getter>
        get() = map.values.map {
            Getter(it)
        }
    override val keys: Set<Any>
        get() = map.keys
    override val size: Int
        get() = map.size

    override fun containsKey(key: Any) = map.containsKey(key)

    override fun isEmpty() = map.isEmpty()

    override fun containsValue(value: Getter) = map.containsValue(
            value.getRaw())

    override fun forEach(
            action: BiConsumer<in Any, in Getter>) = map.forEach { key, value ->
        action.accept(key, Getter(value))
    }

    override fun get(key: Any): Getter? = map[key].let {
        if (null == it) null else Getter(
                it)
    }

    fun <T> getAs(key: Any) = get(key)!!.getAs<T>()

    override fun getOrDefault(key: Any, defaultValue: Getter): Getter {
        val value = map[key]
        return if (null == value) defaultValue else Getter(
                value)
    }
}

class TypedEntries(private val entries: Set<Entry<Any, Any>>) :
        Set<Entry<Any, Getter>> {
    override val size: Int
        get() = entries.size

    override fun contains(element: Entry<Any, Getter>) =
            entries.contains(SimpleEntry(element.key, element.value.getRaw()))

    override fun containsAll(elements: Collection<Entry<Any, Getter>>) =
            entries.containsAll(
                    elements.map { SimpleEntry(it.key, it.value.getRaw()) })

    override fun isEmpty() = entries.isEmpty()

    override fun iterator() = entries.map {
        SimpleEntry(it.key, Getter(it
                .value))
    }.iterator()
}
