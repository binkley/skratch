package hm.binkley.labs.skratch.generics

import java.util.Objects

internal open class Layers<L : Layer<L>, M : MutableLayer<M>>(
    private val _layers: MutableList<M>,
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

    override fun hashCode() = Objects.hash(layers)
    override fun toString() = "${this::class.simpleName}$layers"
}
