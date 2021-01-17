package hm.binkley.labs.skratch.safety

import java.lang.reflect.Field
import java.nio.BufferUnderflowException
import java.nio.ByteBuffer

/** @todo Syntactic sugar causes cancer of the semicolon */
internal fun <T> ByteBuffer.assertEnoughData(
    clazz: Class<T>,
    block: ByteBuffer.(Class<T>) -> T,
) = try {
    this.block(clazz)
} catch (e: BufferUnderflowException) {
    throw AssertionError("Missing bytes: possibly truncated or corrupted, or class version changed")
}

internal fun ByteBuffer.assertSentinel() = get().also {
    assert(0.toByte() == it) {
        "Corrupted sentinel byte: ${it.pretty()} @ ${position() - 1}"
    }
}

internal fun <T> ByteBuffer.assertClassName(expectedClass: Class<T>) {
    val expectedClassName = expectedClass.name
    val actualClassName = readString()
    assert(expectedClassName == actualClassName) {
        "TODO: Supertype and interfaces for target expected class: expected $expectedClassName; got $actualClassName"
    }
}

internal fun assertIntLength(actualLength: Int) {
    assert(Int.SIZE_BYTES == actualLength) {
        "Expected int to be ${Int.SIZE_BYTES} bytes, not $actualLength"
    }
}

internal fun <T> assertFieldCount(
    clazz: Class<T>,
    actualFieldCount: Int,
) {
    val expectedFieldCount = clazz.serializedFields.size
    assert(expectedFieldCount == actualFieldCount) {
        "Field counts changed between class versions: expected $expectedFieldCount; got $actualFieldCount"
    }
}

internal fun ByteBuffer.assertFieldTypeName(field: Field) {
    val expectedFieldTypeName = field.type.name
    val actualFieldTypeName = readString()
    assert(expectedFieldTypeName == actualFieldTypeName) {
        "Field type changed between class versions: expected $expectedFieldTypeName; got $actualFieldTypeName"
    }
}

internal fun ByteBuffer.assertComplete() = assert(0 == remaining()) {
    "Extra bytes remaining after object read from buffer: ${
        slice().array().pretty()
    } @ ${position() - 1}"
}
