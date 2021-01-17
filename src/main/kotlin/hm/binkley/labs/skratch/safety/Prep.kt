package hm.binkley.labs.skratch.safety

import java.lang.reflect.Field
import java.nio.ByteBuffer

internal typealias Prep = Pair<Int, (ByteBuffer) -> ByteBuffer>

internal val Prep.allocateSize
    get() = Int.SIZE_BYTES + (if (-1 == first) 0 else first) + 1

internal fun Prep.writeTo(buf: ByteBuffer) = buf.apply {
    putInt(first)
    second(this)
    put(0)
}

internal fun <T> T?.study(): Prep = when (this) {
    null -> -1 to { it }
    is Boolean -> Byte.SIZE_BYTES to { it.put(if (this) 1 else 0) }
    is Byte -> Byte.SIZE_BYTES to { it.put(this) }
    is Char -> Char.SIZE_BYTES to { it.putChar(this) }
    is Double -> Double.SIZE_BYTES to { it.putDouble(this) }
    is Float -> Float.SIZE_BYTES to { it.putFloat(this) }
    is Int -> Int.SIZE_BYTES to { it.putInt(this) }
    is Long -> Long.SIZE_BYTES to { it.putLong(this) }
    is Short -> Short.SIZE_BYTES to { it.putShort(this) }
    is String -> with(encodeToByteArray()) {
        size to { it.put(this) }
    }
    else -> TODO("Other types? Recursion for embedded objs: $this")
}
