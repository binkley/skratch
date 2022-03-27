package hm.binkley.labs.skratch.layers

class TestLayer(
    map: Map<String, ValueOrRule<Int>> = emptyMap(),
) : MutableLayer<String, Int, TestLayer>(map) {
    override fun <N : TestLayer> duplicate(): N = TestLayer(toMap()).self()
}

class TestLayers(
    history: List<TestLayer> = emptyList(),
) : MutableLayers<String, Int, TestLayer>(history) {
    constructor(firstLayer: TestLayer) : this(listOf(firstLayer))
    constructor(block: EditMap<String, Int>.() -> Unit) : this() {
        push(block)
    }

    override fun new() = TestLayer()
}
