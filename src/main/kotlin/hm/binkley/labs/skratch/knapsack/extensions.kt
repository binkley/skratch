package hm.binkley.labs.skratch.knapsack

import hm.binkley.labs.skratch.knapsack.Value.DatabaseValue

fun Database.value(value: String) = DatabaseValue(this, value)

