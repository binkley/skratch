package hm.binkley.labs.skratch.generics

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
internal open class MutableLayer<M : MutableLayer<M>>(
    _map: MutableMap<String, Any> = mutableMapOf(),
) : Layer<M>(_map),
    MutableMap<String, Any> by _map {
    fun edit(block: MutableMap<String, Any>.() -> Unit) = this.block()
}
