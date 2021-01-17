package hm.binkley.labs.skratch.safety

internal fun Byte.pretty() = "\\x%02x".format(this)
internal fun ByteArray.pretty() = joinToString(" ", "[", "]") {
    it.pretty()
}
