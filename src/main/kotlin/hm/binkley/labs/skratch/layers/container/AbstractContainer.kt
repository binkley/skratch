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
    protected val _contents: MutableList<M> = mutableListOf(),
) : MutableLayer<K, V, C>(index, map) {
    val contents: Collection<M> = _contents

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
