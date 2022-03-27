package hm.binkley.labs.skratch.layers

import hm.binkley.util.Stack
import hm.binkley.util.emptyStack

class TestLayer(
    map: Map<String, ValueOrRule<Int>> = emptyMap(),
) : MutableLayer<String, Int, TestLayer>(map) {
    override fun <N : TestLayer> duplicate(): N =
        TestLayer(toMap()).self()
}

class TestLayers(
    layers: Stack<TestLayer> = emptyStack(),
) : MutableLayers<String, Int, TestLayer>(layers) {
    constructor(block: EditMap<String, Int>.() -> Unit) : this() {
        push(block)
    }

    override fun new() = TestLayer()
}
