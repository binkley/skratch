package hm.binkley.labs.skratch.collections

interface Database {
    fun remove(layer: Int, key: String)
    fun upsert(layer: Int, key: String, value: String)
}
