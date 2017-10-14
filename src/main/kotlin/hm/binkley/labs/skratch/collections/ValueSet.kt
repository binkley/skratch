package hm.binkley.labs.skratch.collections

import hm.binkley.labs.skratch.collections.Value.Nonce

class ValueSet(
        val layer: Int,
        private val set: MutableSet<ValueEntry> = HashSet())
    : AbstractMutableSet<ValueEntry>() {
    override val size: Int
        get() = set.size

    override fun iterator(): MutableIterator<ValueEntry>
            = object : MutableIterator<ValueEntry> {
        private val it = set.iterator()
        private lateinit var next: ValueEntry

        override fun hasNext() = it.hasNext()

        override fun next(): ValueEntry {
            next = it.next()
            return next
        }

        override fun remove() {
            it.remove()
            next.remove(layer)
        }
    }

    override fun add(element: ValueEntry): Boolean {
        val previous = set.find { it.key == element.key }
        return if (null == previous)
            addNew(element)
        else
            replaceWith(previous, element)
    }

    private fun addNew(element: ValueEntry) = when (element.value) {
        Nonce -> false
        else -> {
            val add = set.add(element)
            element.add(layer)
            add
        }
    }

    private fun replaceWith(previous: ValueEntry, element: ValueEntry)
            = when (element.value) {
        Nonce -> remove(previous)
        previous.value -> false
        else -> {
            val add = set.add(element)
            previous.replaceWith(layer, element)
            element.add(layer)
            add
        }
    }
}
