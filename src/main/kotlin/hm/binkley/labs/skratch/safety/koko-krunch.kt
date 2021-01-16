package hm.binkley.labs.skratch.safety

import sun.misc.Unsafe
import java.lang.reflect.Field
import java.lang.reflect.Modifier.isStatic
import java.lang.reflect.Modifier.isTransient
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer
import java.nio.ByteBuffer.allocate

inline fun <reified T> ByteArray.read(): T = read(T::class.java)

/** @todo Freeze/thaw supertype fields */
fun <T> ByteArray.read(clazz: Class<T>): T = try {
    val buf = ByteBuffer.wrap(this)

    val instance = clazz.readFrom(buf) { blankInstance(it) }
    val fieldCount = clazz.readFrom(buf) { fieldCount(it) }

    for (n in 1..fieldCount) {
        val (field, value) = clazz.readFrom(buf) { nextField(it) }
        field.set(instance, value)
    }

    buf.assertSentinel()
    buf.assertComplete()

    instance
} catch (e: BufferUnderflowException) {
    throw AssertionError("Missing bytes: possibly truncated or corrupted, or class version changed")
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
 *   3. The field value for the serialized object if primitive or non-null
 * - If the field value is `null`, use -1 as the value payload byte count, and
 *   do not write a value
 */
fun Any.write(): ByteArray {
    val fields = this::class.java.serializedFields
    val fieldPreps = fields.sortedWith { a, b ->
        a.name.compareTo(b.name)
    }.map {
        ClassInfo(it, this)
    }.flatMap {
        it.study()
    }

    val preps = listOf(
        this::class.java.name.study(),
        fields.size.study()
    ) + fieldPreps
    val buf = allocate(preps.map { it.allocateSize }.sum() + 1)

    preps.forEach { it.writeTo(buf) }
    buf.put(0)

    return buf.array()
}

private val <T> Class<T>.serializedFields
    get() = declaredFields.filterNot {
        it.isStatic || it.isTransient
    }.onEach {
        it.isAccessible = true
    }

private fun <T> Class<T>.getSerializedField(name: String) = try {
    getDeclaredField(name).apply {
        isAccessible = true
    }
} catch (e: NoSuchFieldException) {
    throw AssertionError("Bad field name: $name")
}

/** @todo Syntactic sugar causes cancer of the semicolon */
private fun <T, R> Class<T>.readFrom(
    buf: ByteBuffer,
    block: ByteBuffer.(Class<T>) -> R,
): R = buf.block(this)

private fun <T> ByteBuffer.blankInstance(expectedClass: Class<T>): T {
    val expectedClassName = expectedClass.name
    val actualClassName = readString()
    assert(expectedClassName == actualClassName) {
        "TODO: Supertype and interfaces for target expected class: expected $expectedClassName; got $actualClassName"
    }
    @Suppress("UNCHECKED_CAST")
    return unsafe.allocateInstance(expectedClass) as T
}

/**
 * @todo Structure as a ByteBuffer extension method:
 *       1. Read field count initially, returning an `Iterable<Field>`
 *       2. Assertion that iterator does not run out of fields
 *       3. After iterator finishes, assertion method on field count found
 */
private fun <T> ByteBuffer.fieldCount(expectedClass: Class<T>) =
    readInt().also {
        val expectedFieldCount = expectedClass.serializedFields.size
        assert(expectedFieldCount == it) {
            "Field counts changed between class versions: expected $expectedFieldCount; got $it"
        }
    }

private fun <T> ByteBuffer.nextField(clazz: Class<T>): Pair<Field, Any?> {
    val field = readField(clazz)

    val len = int
    val value: Any?
    if (-1 == len) {
        value = null
    } else {
        value = when (field.type.name) {
            Boolean::class.java.name -> 0.toByte() != get()
            Byte::class.java.name -> get()
            Char::class.java.name -> char
            Double::class.java.name -> double
            Float::class.java.name -> float
            Int::class.java.name -> int
            Long::class.java.name -> long
            String::class.java.name -> ByteArray(len).let {
                get(it)
                String(it)
            }
            else -> TODO("All the rest")
        }
    }

    assertSentinel()

    return field to value
}

private val unsafe = Unsafe::class.java.getDeclaredField("theUnsafe").apply {
    isAccessible = true
}.get(null) as Unsafe

private fun ByteBuffer.readString() = ByteArray(int).let {
    get(it)
    assertSentinel()
    String(it)
}

private fun ByteBuffer.readInt(): Int {
    val len = int
    assert(Int.SIZE_BYTES == len) {
        "Expected int to be ${Int.SIZE_BYTES} bytes, not $len"
    }
    return int.also {
        assertSentinel()
    }
}

private val Field.isStatic get() = isStatic(modifiers)
private val Field.isTransient get() = isTransient(modifiers)

private typealias Prep = Pair<Int, (ByteBuffer) -> ByteBuffer>

private val Prep.allocateSize
    get() = Int.SIZE_BYTES + (if (-1 == first) 0 else first) + 1

private fun Prep.writeTo(buf: ByteBuffer) = buf.apply {
    putInt(first)
    second(this)
    put(0)
}

private fun <T> T?.study(): Prep = when (this) {
    null -> -1 to { it }
    is Boolean -> Byte.SIZE_BYTES to { it.put(if (this) 1 else 0) }
    is Byte -> Byte.SIZE_BYTES to { it.put(this) }
    is Char -> Char.SIZE_BYTES to { it.putChar(this) }
    is Double -> Double.SIZE_BYTES to { it.putDouble(this) }
    is Float -> Float.SIZE_BYTES to { it.putFloat(this) }
    is Int -> Int.SIZE_BYTES to { it.putInt(this) }
    is Long -> Long.SIZE_BYTES to { it.putLong(this) }
    is Short -> Short.SIZE_BYTES to { it.putShort(this) }
    is String -> {
        val bytes = encodeToByteArray()
        bytes.size to { it.put(bytes) }
    }
    else -> TODO("Other types? Recursion for embedded objs: $this")
}

private data class ClassInfo(
    val name: String,
    val typeName: String,
    val value: Any?,
) {
    constructor(field: Field, o: Any)
            : this(field.name, field.type.name, field.get(o))

    fun study() = listOf(
        name,
        typeName,
        value
    ).map { it.study() }
}

private fun ByteBuffer.assertSentinel() = get().also {
    assert(0.toByte() == it) {
        "Corrupted sentinel byte: ${it.pretty()} @ ${position() - 1}"
    }
}

private fun <T> ByteBuffer.readField(clazz: Class<T>) =
    readString().let { fieldName ->
        val field = try {
            clazz.getSerializedField(fieldName)
        } catch (e: NoSuchFieldException) {
            throw AssertionError("Bad field name: $fieldName")
        }
        val expectedFieldTypeName = field.type.name
        val actualFieldTypeName = readString()
        assert(expectedFieldTypeName == actualFieldTypeName) {
            "Field type changed between class versions: expected $expectedFieldTypeName; got $actualFieldTypeName"
        }
        field
    }

private fun ByteBuffer.assertComplete() = assert(0 == remaining()) {
    "Extra bytes remaining after object read from buffer: ${
        slice().array().pretty()
    } @ ${position() - 1}"
}

internal fun Byte.pretty() = "\\x%02x".format(this)
internal fun ByteArray.pretty() = joinToString(" ", "[", "]") {
    it.pretty()
}
