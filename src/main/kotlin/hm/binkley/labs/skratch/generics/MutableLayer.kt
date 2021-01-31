package hm.binkley.labs.skratch.generics

@Suppress("DELEGATED_MEMBER_HIDES_SUPERTYPE_OVERRIDE")
internal open class MutableLayer<M : MutableLayer<M>>(
    map: MutableMap<String, Entry<*>> = mutableMapOf(),
) : Layer<M>(map),
    MutableMap<String, Entry<*>> by map {
    fun edit(block: MutableMap<String, Entry<*>>.() -> Unit): M {
        block()
        return self
    }
}
