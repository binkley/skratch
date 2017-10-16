package hm.binkley.labs.skratch.knapsack

interface Database {
    fun remove(layer: Int, key: String)
    fun upsert(layer: Int, key: String, value: String)
}
