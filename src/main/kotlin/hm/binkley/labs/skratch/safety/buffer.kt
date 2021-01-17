package hm.binkley.labs.skratch.safety

import java.nio.ByteBuffer

/** @todo Syntactic sugar causes cancer of the semicolon */
internal fun <T, R> Class<T>.readFrom(
    buf: ByteBuffer,
    block: ByteBuffer.(Class<T>) -> R,
): R = buf.block(this)

internal fun ByteBuffer.readString() = ByteArray(int).let {
    get(it)
    assertSentinel()
    String(it)
}

internal fun ByteBuffer.readInt() = int.let {
    assertIntLength(it)
    int.also {
        assertSentinel()
    }
}
