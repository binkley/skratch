package hm.binkley.labs.skratch.collections

import hm.binkley.labs.skratch.collections.Value.DatabaseValue

fun Database.value(value: String) = DatabaseValue(this, value)

