package hm.binkley.labs.skratch.layers

import hm.binkley.util.emptyStack

class TestLayer(
    map: Map<String, ValueOrRule<Int>> = emptyMap(),
) : MutableLayer<String, Int, TestLayer>(map) {
    override fun <N : TestLayer> duplicate(): N =
        TestLayer(toMap()).self()
}

class TestLayers : Layers<String, Int, TestLayer>(emptyStack())
