package hm.binkley.labs.skratch.safety

import org.junit.jupiter.api.Test
import java.nio.ByteBuffer
import kotlin.test.assertEquals

private val original = TestFoo(
    s = "BOB",
    b = 3.toByte(),
    i = 13)

internal class KokoKrunchTest {
    @Test
    fun `should record a byte`() {
        val buf = ByteBuffer.wrap(original.write())

        assertEquals(original::class.java.name.length, buf.int)
        var tmp = ByteArray(original::class.java.name.length)
        buf.get(tmp)
        assertEquals(original::class.java.name, String(tmp))
        assertEquals(0, buf.get())

        assertEquals(Int.SIZE_BYTES, buf.int)
        assertEquals(3, buf.int)
        assertEquals(0, buf.get())

        assertEquals("b".length, buf.int)
        tmp = ByteArray("b".length)
        buf.get(tmp)
        assertEquals("b", String(tmp))
        assertEquals(0, buf.get())

        assertEquals("byte".length, buf.int)
        tmp = ByteArray("byte".length)
        buf.get(tmp)
        assertEquals("byte", String(tmp))
        assertEquals(0, buf.get())

        assertEquals(1, buf.int)
        assertEquals(original.b, buf.get())
        assertEquals(0, buf.get())

        assertEquals("i".length, buf.int)
        tmp = ByteArray("i".length)
        buf.get(tmp)
        assertEquals("i", String(tmp))
        assertEquals(0, buf.get())

        assertEquals("int".length, buf.int)
        tmp = ByteArray("int".length)
        buf.get(tmp)
        assertEquals("int", String(tmp))
        assertEquals(0, buf.get())

        assertEquals(Int.SIZE_BYTES, buf.int)
        assertEquals(original.i, buf.int)
        assertEquals(0, buf.get())

        assertEquals("s".length, buf.int)
        tmp = ByteArray("s".length)
        buf.get(tmp)
        assertEquals("s", String(tmp))
        assertEquals(0, buf.get())

        assertEquals("java.lang.String".length, buf.int)
        tmp = ByteArray("java.lang.String".length)
        buf.get(tmp)
        assertEquals("java.lang.String", String(tmp))
        assertEquals(0, buf.get())

        assertEquals(original.s.length, buf.int)
        tmp = ByteArray(original.s.length)
        buf.get(tmp)
        assertEquals(original.s, String(tmp))
        assertEquals(0, buf.get())

        assertEquals(0, buf.get())
        assertEquals(0, buf.remaining())
    }
}

private data class TestFoo(
    val s: String,
    val b: Byte,
    val i: Int,
)
