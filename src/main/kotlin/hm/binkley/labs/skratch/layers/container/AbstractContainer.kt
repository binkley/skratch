package hm.binkley.labs.skratch.layers.container

import hm.binkley.labs.skratch.layers.MutableLayer
import hm.binkley.labs.skratch.layers.NewLayer
import org.checkerframework.checker.units.qual.K

/**
 * @todo Limits for containers
 * @todo Sort out what equality means for layers:
 *       Adding 2 foo (OK) _vs_ adding the same foo twice (error)
 * @todo Set: No duplicate layers; order by layer index
 * @todo Needs to be a MIXIN, not a base class; Kotlin does not do multiple
 *       inheritance
 */
interface AbstractContainer<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>,
    C,
    > where C : MutableLayer<K, V, C>, C : AbstractContainer<K, V, M, C> {
    val contents: List<M>

    fun update(contents: List<M>): NewLayer<K, V, C>

    /** @return a new layer with updated contents, preserving the original */
    operator fun plus(layer: M): NewLayer<K, V, C> {
        require(layer !in contents) { "Already in container: $layer" }
        return update(contents + layer)
    }

    /** @return a new layer with updated contents, preserving the original */
    operator fun minus(layer: M): NewLayer<K, V, C> {
        require(layer in contents) { "Not in container: $layer" }
        return update(contents - layer)
    }
}
