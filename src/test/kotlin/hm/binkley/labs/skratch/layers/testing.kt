package hm.binkley.labs.skratch.layers

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

    override fun <N : TestLayer> duplicate(): N = TestLayer(toMap()).self()
}

/**
 * Canonical constructors:
 * - `TestLayers()` - defaults to an empty first layer
 * - `TestLayers(firstLayer)` - provides a valid first layer
 * - `TestLayers(block)` - provides valid edits to an empty first layer
 * - `TestLayers(history)` - provides a valid history that includes a first
 *   layer
 */
class TestLayers(
    history: List<TestLayer> = listOf(TestLayer()),
) : MutableLayers<String, Int, TestLayer>(history) {
    constructor(initialRules: TestLayer) : this(listOf(initialRules))
    constructor(block: EditMap<String, Int>.() -> Unit) : this() {
        edit(block)
    }

    override fun new() = TestLayer()
}
