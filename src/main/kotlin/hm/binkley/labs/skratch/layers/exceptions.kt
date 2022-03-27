package hm.binkley.labs.skratch.layers

class MissingRuleException(key: Any) : Exception("No rule for key: $key")

class MissingValuesException(key: Any) : Exception("No values for key: $key")

object MissingFirstLayerException :
    Exception("No first layer for initial rules")
