package hm.binkley.labs.skratch.layers

abstract class LayersException(message: String) : Exception(message)

class MissingRuleException(key: Any) :
    LayersException("No rule for key: $key")

class MissingValuesException(key: Any) :
    LayersException("No values for key, and rule provides no default: $key")

object MissingFirstLayerException :
    LayersException("No first layer to hold initial rules")

class MissingKeyException(key: Any) :
    LayersException("No value or rule for key: $key")

class NotAValueException(key: Any) :
    LayersException("Not a value: $key")
