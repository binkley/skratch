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
    map: Map<String, ValueOrRule<Int>> = emptyMap(),
) : MutableLayer<String, Int, TestLayer>(map) {
    constructor(block: EditMap<String, Int>.() -> Unit) : this() {
        edit(block)
    }

    override fun <N : TestLayer> copy(): N = TestLayer(toMap()).self()
}

/**
 * Canonical constructors:
 * - `TestLayers()` - defaults to an empty first layer
 * - `TestLayers(firstLayer)` - provides a valid first layer
 * - `TestLayers(block)` - provides valid edits to an empty first layer
 * - `TestLayers(history)` - provides a valid history that includes a first
 * - `TestLayers(history...)` - provides a valid history that includes a first
 *   layer
 */
class TestLayers(
    history: List<TestLayer> = listOf(TestLayer()),
) : MutableLayers<String, Int, TestLayer>(::TestLayer, history) {
    constructor(initialRules: TestLayer) : this(listOf(initialRules))
    constructor(block: EditMap<String, Int>.() -> Unit) : this() {
        edit(block)
    }
    constructor(vararg history: TestLayer) : this(history.toList())
}

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
