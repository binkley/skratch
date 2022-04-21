package hm.binkley.labs.skratch.layers.container

import hm.binkley.labs.skratch.layers.Layer
import hm.binkley.labs.skratch.layers.MutableLayer
import hm.binkley.labs.skratch.layers.ValueOrRule
import hm.binkley.labs.skratch.layers.self

/**
 * @todo Limits for containers
 * @todo Sort out what equality means for layers:
 *       Adding 2 foo (OK) _vs_ adding the same foo twice (error)
 * @todo Set: No duplicate layers; order by layer index
 */
abstract class AbstractContainer<
    K : Any,
    V : Any,
    L : Layer<K, V, L>,
    C : AbstractContainer<K, V, L, C>,
    >(
    index: Int,
    map: Map<K, ValueOrRule<V>> = emptyMap(),
    private val _contents: MutableList<L> = mutableListOf(),
) : MutableLayer<K, V, C>(index, map) {
    val contents: List<L> = _contents

    operator fun plus(layer: L): C {
        if (layer in contents) error("Already in container: $layer")
        _contents += layer
        return self()
    }

    operator fun minus(layer: L): C {
        if (layer !in contents) error("Not in container: $layer")
        _contents -= layer
        return self()
    }
}
