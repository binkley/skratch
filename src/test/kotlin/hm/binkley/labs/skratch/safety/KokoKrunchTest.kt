package hm.binkley.labs.skratch.safety

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

private val written = TestFoo(
    b = 3.toByte(),
    d = null,
    i = 13,
    s = "BOB",
)

internal class KokoKrunchTest {
    @Test
    fun `should round trip`() {
        val bytes = written.write()
        val read = bytes.read<TestFoo>()

        assertEquals(written, read)
    }

    @Test
    fun `should complain on bad sentinel`() {
        val bytes = written.write().apply {
            this[size - 1] = 1.toByte()
        }

        assertThrows<AssertionError> {
            bytes.read<TestFoo>()
        }
    }

    @Test
    fun `should complain on extra bytes`() {
        val bytes = with(written.write()) {
            copyOf(size + 1)
        }

        assertThrows<AssertionError> {
            bytes.read<TestFoo>()
        }
    }
}

private data class TestFoo(
    val s: String,
    val b: Byte,
    val i: Int,
    val d: Double?,
)
