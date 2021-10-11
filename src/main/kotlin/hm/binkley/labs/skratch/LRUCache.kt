package hm.binkley.labs.skratch

/**
 * Implements an LRU cache.  This cache has no configurable strategies for
 * eviction. The cache has a [capacity], and when full, evicts last accessed
 * entries upon addition of new entries.
 *
 * Note that it is an extension of [LinkedHashMap] with the default load
 * factor (0.75).
 *
 * It is a bug in the [LinkedHashMap] API that `accessOrder` is a boolean
 * rather than an enum. `true` for `accessOrder` means use access order;
 * `false` means use insertion order.  A more clear API would make these
 * explicit in the enum value namings.
 *
 * *NB* &mdash; hard to grok from the docs, but access order in
 * [LinkedHashMap] is from _last recently used_ to _most recently used_.
 *
 * @todo Does not consider concurrent access
 */
class LRUCache(
    /**
     * How many LRU entries to keep?  Capacity must be non-negative. Size of
     * 0 (zero) means no caching.
     */
    private val capacity: Int,
    /** What value to provide on a cache miss? */
    private val cacheMiss: Int = -1,
) : LinkedHashMap<Int, Int>(
    capacity,
    0.75f, // The default: not available as a public constant
    true, // Use access order (old to new), not insertion order
) {
    init {
        if (0 > capacity) throw IllegalArgumentException(
            "Capacity for LRUCache must be non-negative: $capacity"
        )
    }

    /**
     * Checks if the LRU cache is at maximum [capacity].
     *
     * @see [isEmpty]
     */
    fun isFull() = capacity == size

    override fun get(key: Int): Int = super.get(key) ?: cacheMiss

    override fun put(key: Int, value: Int): Int? {
        maybeRemoveOldestEntry()

        return super.put(key, value)
    }

    private fun maybeRemoveOldestEntry() {
        if (isFull()) remove(keys.first()) // Keys are in old-to-new order
    }
}
