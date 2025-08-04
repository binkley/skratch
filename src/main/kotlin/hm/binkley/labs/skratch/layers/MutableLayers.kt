package hm.binkley.labs.skratch.layers

import hm.binkley.util.top

fun interface NewLayer<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>
> {
    operator fun invoke(index: Int): M
}

// TODO: Messy relationships among "push", "edit", and "whatIf"
interface MutableLayers<
    K : Any,
    V : Any,
    M : MutableLayer<K, V, M>
> : Layers<K, V, M> {
    fun pop(): M

    fun <N : M> push(newLayer: NewLayer<K, V, M>): N

    /** Creates a [NewLayer], edits it, and returns it. */
    fun push(block: EditMap<K, V>.() -> Unit): M

    /** Edits the [top] layer, and returns it. */
    fun edit(block: EditMap<K, V>.() -> Unit): M
}
