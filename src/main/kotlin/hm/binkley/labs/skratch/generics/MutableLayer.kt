package hm.binkley.labs.skratch.generics

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
internal open class MutableLayer<M : MutableLayer<M>>(
    map: MutableMap<String, Any> = mutableMapOf(),
) : Layer<M>(map),
    MutableMap<String, Any> by map {
    fun edit(block: MutableMap<String, Any>.() -> Unit): M {
        block()
        return self
    }
}
