package hm.binkley.labs.skratch.layers

abstract class LayersException(message: String) : Exception(message)

class MissingRuleException(key: Any) :
    LayersException("No rule for key: $key")

class MissingValuesException(key: Any) :
    LayersException("No values for key: $key")

object MissingFirstLayerException :
    LayersException("No first layer for initial rules")
