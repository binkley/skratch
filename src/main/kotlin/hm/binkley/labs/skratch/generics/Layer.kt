package hm.binkley.labs.skratch.generics

import java.util.Objects.hash

internal open class Layer<L : Layer<L>>(
    protected val map: MutableMap<String, Any> = mutableMapOf(),
) : Map<String, Any> by map {
    @Suppress("UNCHECKED_CAST")
    val self: L
        get() = this as L

    override fun equals(other: Any?) = this === other ||
            other is Layer<*> &&
            map == other.map

    override fun hashCode() = hash(map)
    override fun toString() = "${this::class.simpleName}$map"
}
