package hm.binkley.labs.skratch

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * See [LeetCode](https://leetcode.com/problems/lru-cache/).  The page
 * describes inputs and outputs thus:
 * ```
 * Input:
 * ["LRUCache", "put", "put", "get", "put", "get", "put", "get", "get", "get"]
 * [[2], [1, 1], [2, 2], [1], [3, 3], [2], [4, 4], [1], [3], [4]]
 * Output:
 * [null, null, null, 1, null, -1, null, -1, 3, 4]
 * ```
 *
 * Translating input/out to calls and test expectations:
 * * `cache = LURCache(2)` // constructor
 * * `cache[1] = 1`
 * * `cache[2] = 2` // cache now full; `2` is most recently used
 * * `cache[1]` returns `1` // `1` is now most recently used
 * * `cache[3] = 3` // evicts `2` as `1` was more recently used; `3` is
 *   most recently used
 * * `cache[2]` returns `-1` // evicted
 * * `cache[4] = 4` // evicts `1`; `4` is most recently used
 * * `cache[1]` returns `-1` // evicted
 * * `cache[3]` returns `3`
 * * `cache[4]` returns `4`
 *
 * @todo Test that when cache is full, updating an existing entry does not
 *       evict the last used entry
 */
internal class LRUCacheTest {
    @Test
    fun `should LRU`() {
        val cache = LRUCache(2)

        fun assertCached(key: Int) = assertEquals(key, cache[key])

        fun assertEvicted(key: Int) = assertEquals(-1, cache[key])

        cache[1] = 1
        cache[2] = 2

        assertCached(1)

        cache[3] = 3

        assertEvicted(2)

        cache[4] = 4

        assertEvicted(1)
        assertCached(3)
        assertCached(4)
    }
}
