package hm.binkley.labs.skratch.safety

import java.lang.reflect.Field
import java.lang.reflect.InaccessibleObjectException
import java.lang.reflect.Modifier.isStatic
import java.lang.reflect.Modifier.isTransient
import java.nio.ByteBuffer
import java.nio.ByteBuffer.allocate

fun main() {
    val cereal = Cereal(3, "THREE")
    println("$cereal -> ${cereal.write().pretty()}")
}

// TODO: Richer return type -- eg, ByteArray, streams, et al
private fun Any.write(): ByteArray {
    val type = this::class.java.name
    val typePrep = type.prep()

    val fields = this::class.java.declaredFields.filterNot {
        it.isStatic || it.isTransient
    }
    val fieldCountPrep = fields.size.prep()

    val fieldBufs = fields.onEach {
        if (!it.trySetAccessible())
            throw InaccessibleObjectException("Cannot read: $it")
    }.sortedWith { a, b ->
        a.name.compareTo(b.name)
    }.map {
        it.write(this)
    }

    val buf = allocate(
        typePrep.allocateSize +
                fieldCountPrep.allocateSize +
                fieldBufs.map { it.size }.sum() +
                1)

    typePrep.writeTo(buf)
    fieldCountPrep.writeTo(buf)
    fieldBufs.forEach { buf.put(it) }
    buf.put(0)

    return buf.array()
}

private val Field.isStatic get() = isStatic(modifiers)
private val Field.isTransient get() = isTransient(modifiers)

typealias Prep = Pair<Int, (Int, ByteBuffer) -> ByteBuffer>

private val Prep.allocateSize get() = Int.SIZE_BYTES + first + 1
private fun Prep.writeTo(buf: ByteBuffer) = second(first, buf)

private fun <T> T.prep(): Prep = when (this) {
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

private fun Field.write(o: Any): ByteArray {
    val namePrep = name.prep()
    val typePrep = type.name.prep()
    val valuePrep = get(o).prep()

    val buf = allocate(
        namePrep.allocateSize +
                typePrep.allocateSize +
                valuePrep.allocateSize +
                1)

    namePrep.writeTo(buf)
    typePrep.writeTo(buf)
    valuePrep.writeTo(buf)
    buf.put(0)

    return buf.array()
}

private fun ByteArray.pretty() = joinToString(" ", "[", "]") {
    "\\x%02x".format(it)
}

private data class Cereal(val i: Int, val s: String)
