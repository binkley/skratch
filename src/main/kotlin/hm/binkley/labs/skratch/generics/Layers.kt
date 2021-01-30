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

    companion object {
        fun <L : Layer<L>, M : MutableLayer<M>> new(layers: List<M>) =
            Layers<L, M>(layers.toMutableList())

        fun <L : Layer<L>, M : MutableLayer<M>> new(
            ctor: () -> M,
            block: MutableMap<String, Any>.() -> Unit,
        ) = new<L, M>(mutableListOf(ctor().edit(block)))

        fun <L : Layer<L>, M : MutableLayer<M>> new(
            ctor: () -> M,
            vararg firstLayer: Pair<String, Any>,
        ) = new<L, M>(ctor) {
            firstLayer.forEach {
                this[it.first] = it.second
            }
        }
    }
}
