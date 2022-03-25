package hm.binkley.labs.skratch.layers

class InvalidFirstLayerException(firstLayer: Layer<*, *, *>) :
    Exception("First layer contains values: $firstLayer")

class MissingRuleException(key: Any) : Exception("No rule for key: $key")

class MissingValuesException(key: Any) : Exception("No values for key: $key")
