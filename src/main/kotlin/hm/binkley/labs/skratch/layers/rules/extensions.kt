@file:Suppress("unused")

package hm.binkley.labs.skratch.layers.rules

import hm.binkley.labs.skratch.layers.EditMap

// Extension receiver unused but needed for:
// - Scope management
// - Type inference

fun <K : Any, V : Any, T : V>
EditMap<K, V>.lastRule() = LastRule<K, V, T>()

fun <K : Any, V : Any, T : V>
EditMap<K, V>.lastOrDefaultRule(default: T) =
    LastOrDefaultRule<K, V, T>(default)

fun <K : Any, V : Any, T : V>
EditMap<K, V>.lastOrNullRule() = LastOrNullRule<K, V, T>()
