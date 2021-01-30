package hm.binkley.labs.skratch.generics

import java.util.Objects.hash

fun main() {
    val x: MutableList<MutableLayer<Q>> = mutableListOf(Q())
    val l = Layers<P, Q>(x)
    println(l)
    l.edit {
        this["a"] = 3
    }
    println(l)
    l.nextLayer { Q() }
    println(l)
}

private open class Layers<L : Layer<L>, M : MutableLayer<M>>(
    private val _layers: MutableList<MutableLayer<M>>,
) {
    @Suppress("UNCHECKED_CAST")
    val layers: List<L>
        get() = _layers as List<L>

    fun edit(block: MutableMap<String, Any>.() -> Unit) =
        _layers.last().edit(block)

    fun nextLayer(new: () -> M): M {
        val it = new()
        _layers.add(it)
        return it
    }

    override fun equals(other: Any?) = this === other ||
            other is Layers<*, *> &&
            layers == other.layers

    override fun hashCode() = hash(layers)
    override fun toString() = "${this::class.simpleName}$layers"
}

private open class Layer<L : Layer<L>>(
    protected val _map: MutableMap<String, Any> = mutableMapOf(),
) : Map<String, Any> by _map {
    @Suppress("UNCHECKED_CAST")
    val self: L
        get() = this as L

    override fun equals(other: Any?) = this === other ||
            other is Layer<*> &&
            _map == other._map

    override fun hashCode() = hash(_map)
    override fun toString() = "${this::class.simpleName}$_map"
}

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
private open class MutableLayer<M : MutableLayer<M>>(
    _map: MutableMap<String, Any> = mutableMapOf(),
) : Layer<M>(_map),
    MutableMap<String, Any> by _map {
    fun edit(block: MutableMap<String, Any>.() -> Unit) = this.block()
}

private class P : Layer<P>()
private class Q : MutableLayer<Q>()
