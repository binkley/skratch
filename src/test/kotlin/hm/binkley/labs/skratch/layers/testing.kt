package hm.binkley.labs.skratch.layers

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe

class TestEditMap(
    val map: MutableMap<String, ValueOrRule<Int>> = mutableMapOf(),
) : EditMap<String, Int>, MutableMap<String, ValueOrRule<Int>> by map

/**
 * Canonical constructors:
 * - `TestLayer()` - defaults to an empty layer
 * - `TestLayer(map)` - provides a layer containing [map] key-value pairs
 * - `TestLayer(block)` - provides a layer with `block` edits
 */
class TestLayer(
    index: Int,
    map: Map<String, ValueOrRule<Int>> = emptyMap(),
) : MutableLayer<String, Int, TestLayer>(index, map) {
    constructor(index: Int, block: EditMap<String, Int>.() -> Unit) :
        this(index) {
        edit(block)
    }

    override fun copy() = TestLayer(index, this)
}

/**
 * Canonical constructors:
 * - `TestLayers()` - defaults to an empty first layer
 * - `TestLayers(firstLayer)` - provides a valid first layer
 * - `TestLayers(block)` - provides valid edits to an empty first layer
 * - `TestLayers(history)` - provides a valid history that includes a first
 *   layer
 * - `TestLayers(history...)` - provides a valid history that includes a first
 *   layer
 */
class TestLayers constructor(
    history: List<TestLayer> = listOf(TestLayer(0)),
) : MutableLayers<String, Int, TestLayer>(history, ::TestLayer) {
    constructor(initialRules: TestLayer) : this(listOf(initialRules))
    constructor(block: EditMap<String, Int>.() -> Unit) : this() {
        edit(block)
    }

    constructor(vararg history: TestLayer) : this(history.asList())
}

/**
 * Convenience for the "BOB" property when in an edit map block.
 * "BOB" is deleteable through assignment of `null`.
 */
var EditMap<String, Int>.BOB: Int? by EditMapDelegate { "BOB" }

/** Convenience for the "NANCY" property when in an edit map block. */
var EditMap<String, Int>.NANCY: Int by EditMapDelegate { "NANCY" }

inline fun <reified E : Throwable> TestLayers.shouldRollback(
    block: (TestLayers) -> Unit,
): E {
    val initView: Map<String, Int> = toMap() // Defensive copy
    val initHistory = history.toList() // Defensive copy
    val e = shouldThrow<E> { block(this) }
    this shouldBe initView
    history shouldBe initHistory
    return e
}
