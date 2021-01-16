package hm.binkley.labs.skratch.safety

import java.lang.reflect.Field
import java.lang.reflect.Modifier.isStatic
import java.lang.reflect.Modifier.isTransient
import java.nio.ByteBuffer
import java.nio.ByteBuffer.allocate

fun main() {
    val cereal = Cereal(s = "THREE", i = 3)
    val bytes = cereal.write()

    println("$cereal -> ${bytes.pretty()}")
    bytes.dump()
}

private fun ByteArray.dump() {
    val buf = ByteBuffer.wrap(this)
    val length = buf.int
    println("CLASS NAME LENGTH -> $length")
    val className = String(buf.slice(buf.position(), length).array())
    println("CLASS NAME -> $className")
    assert(buf.int == 4)
    val fieldCount = buf.int
    println("FIELD COUNT -> $fieldCount")
}

/**
 * Write out byte array representing a serialized object:
 * - Each record has 3 parts:
 *   1. Byte count of the value payload
 *   2. The value payload in bytes
 *   3. A terminating sentinel 0 byte
 * - The complete byte array has an additional terminating sentinel 0 byte
 * - First record is the object class name
 * - Second record is a count of serialized fields
 * - Following records are for each non-static, non-transient field, sorted by
 *   field name alphabetically, each field contributing 3 more records:
 *   1. The field name
 *   2. The field type
 *   3. The field value for the serialized object
 */
fun Any.write(): ByteArray {
    val fields = this::class.java.declaredFields.filterNot {
        it.isStatic || it.isTransient
    }

    val fieldPreps = fields.onEach {
        it.isAccessible = true
    }.sortedWith { a, b ->
        a.name.compareTo(b.name)
    }.flatMap {
        listOf(it.name.study(), it.type.name.study(), it.get(this).study())
    }

    val preps = listOf(this::class.java.name.study(), fields.size.study()) +
            fieldPreps
    val buf = allocate(preps.map { it.allocateSize }.sum() + 1)

    preps.forEach { it.writeTo(buf) }
    buf.put(0)

    return buf.array()
}

private val Field.isStatic get() = isStatic(modifiers)
private val Field.isTransient get() = isTransient(modifiers)

typealias Prep = Pair<Int, (Int, ByteBuffer) -> ByteBuffer>

private val Prep.allocateSize get() = Int.SIZE_BYTES + first + 1
private fun Prep.writeTo(buf: ByteBuffer) = second(first, buf)

private fun <T> T.study(): Prep = when (this) {
    is Byte -> Byte.SIZE_BYTES to { size, buf ->
        buf.putInt(size)
        buf.put(this)
        buf.put(0)
    }
    is Char -> Char.SIZE_BYTES to { size, buf ->
        buf.putInt(size)
        buf.putChar(this)
        buf.put(0)
    }
    is Double -> Double.SIZE_BYTES to { size, buf ->
        buf.putInt(size)
        buf.putDouble(this)
        buf.put(0)
    }
    is Float -> Float.SIZE_BYTES to { size, buf ->
        buf.putInt(size)
        buf.putFloat(this)
        buf.put(0)
    }
    is Int -> Int.SIZE_BYTES to { size, buf ->
        buf.putInt(size)
        buf.putInt(this)
        buf.put(0)
    }
    is Long -> Long.SIZE_BYTES to { size, buf ->
        buf.putInt(size)
        buf.putLong(this)
        buf.put(0)
    }
    is Short -> Short.SIZE_BYTES to { size, buf ->
        buf.putInt(size)
        buf.putShort(this)
        buf.put(0)
    }
    is String -> {
        val bytes = encodeToByteArray()
        bytes.size to { size, buf ->
            buf.putInt(size)
            buf.put(bytes)
            buf.put(0)
        }
    }
    else -> TODO("Other types? Recursion for embedded objs: $this")
}

private fun ByteArray.pretty() = joinToString(" ", "[", "]") {
    "\\x%02x".format(it)
}

private data class Cereal(val s: String, val i: Int)
