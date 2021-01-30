package hm.binkley.labs.skratch.generics

import java.util.Objects

internal open class Layer<L : Layer<L>>(
    protected val _map: MutableMap<String, Any> = mutableMapOf(),
) : Map<String, Any> by _map {
    @Suppress("UNCHECKED_CAST")
    val self: L
        get() = this as L

    override fun equals(other: Any?) = this === other ||
            other is Layer<*> &&
            _map == other._map

    override fun hashCode() = Objects.hash(_map)
    override fun toString() = "${this::class.simpleName}$_map"
}
