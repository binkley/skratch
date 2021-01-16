package hm.binkley.labs.skratch.safety

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

private val written = TestFoo(
    bool = true,
    byte = 3.toByte(),
    d = null,
    i = 13,
    text = "BOB",
)

internal class KokoKrunchTest {
    @Test
    fun `should round trip`() {
        val bytes = written.write()
        val read = bytes.read<TestFoo>()

        assertEquals(written, read)
    }

    @Test
    fun `should complain about bad sentinel`() {
        val bytes = written.write().apply {
            this[size - 1] = 1.toByte()
        }

        assertThrows<AssertionError> {
            bytes.read<TestFoo>()
        }
    }

    @Test
    fun `should complain about extra bytes`() {
        val bytes = with(written.write()) {
            copyOf(size + 1)
        }

        assertThrows<AssertionError> {
            bytes.read<TestFoo>()
        }
    }

    @Test
    fun `should complain about missing fields`() {
        val bytes = written.write()

        assertThrows<AssertionError> {
            bytes.read<TestFoo_NextGen>()
        }
    }
}

private data class TestFoo(
    val text: String,
    val byte: Byte,
    val bool: Boolean,
    val d: Double?,
    val i: Int,
)

private data class TestFoo_NextGen(
    val text: String,
    val byte: Byte,
    val bool: Boolean,
    val d: Double?,
    val i: Int,
    val new: Long,
)
