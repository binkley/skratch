// Extension receiver unused but needed for:
// - Scope management
// - Type inference
@file:Suppress("unused")

package hm.binkley.labs.skratch.layers.rules

import hm.binkley.labs.skratch.layers.EditMap

fun <K : Any, V : Any, T : Comparable<T>> EditMap<K, V>.ceilRule(ceiling: T) = CeilRule<K, V, T>(ceiling)

fun <K : Any, V : Any, T : V> EditMap<K, V>.constantRule(constant: T) = ConstantRule<K, V, T>(constant)

fun <K : Any, V : Any, T : Comparable<T>> EditMap<K, V>.floorRule(floor: T) = FloorRule<K, V, T>(floor)

fun <K : Any, V : Any, T : V> EditMap<K, V>.lastRule() = LastRule<K, V, T>()

fun <K : Any, V : Any, T : V> EditMap<K, V>.lastOrDefaultRule(default: T) = LastOrDefaultRule<K, V, T>(default)

fun <K : Any, V : Any, T : V> EditMap<K, V>.lastOrNullRule() = LastOrNullRule<K, V, T>()
