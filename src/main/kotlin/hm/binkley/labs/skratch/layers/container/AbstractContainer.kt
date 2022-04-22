package hm.binkley.labs.skratch.layers.container

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
    M : MutableLayer<K, V, M>,
    C : AbstractContainer<K, V, M, C>,
    >(
    index: Int,
    map: Map<K, ValueOrRule<V>> = emptyMap(),
    private val _contents: MutableList<M> = mutableListOf(),
) : MutableLayer<K, V, C>(index, map) {
    /**
     * Cleaner would be implementing `Collection<M> by _contents`.
     * However, a layer is also a "Map", and signatures clash between "Map"
     * and "Collection".
     * For example, should `isEmpty()` mean there are no contents, or that
     * this layer has no keys?
     */
    val contents: List<M> = _contents

    operator fun plus(layer: M): C {
        if (layer in contents) error("Already in container: $layer")
        _contents += layer
        return self()
    }

    operator fun minus(layer: M): C {
        if (layer !in contents) error("Not in container: $layer")
        _contents -= layer
        return self()
    }
}
