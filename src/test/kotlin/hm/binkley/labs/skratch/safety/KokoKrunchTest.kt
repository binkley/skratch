package hm.binkley.labs.skratch.safety

import org.junit.jupiter.api.Test
import java.math.BigInteger
import java.math.BigInteger.TWO
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

private val written = TestFoo(
    bint = TWO.pow(1_234),
    bool = true,
    byte = 3.toByte(),
    d = null,
    text = "BOB",
    z = 13,
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

        assertFailsWith<AssertionError> {
            bytes.read<TestFoo>()
        }
    }

    @Test
    fun `should complain about wrong class type`() {
        val bytes = written.write().apply {
            this[indexOf('F'.toByte())] = 'G'.toByte()
        }

        assertFailsWith<AssertionError> {
            bytes.read<TestFoo>()
        }
    }

    @Test
    fun `should complain about wrong int size`() {
        val bytes = written.write().apply {
            this[indexOf(4.toByte())] = 3.toByte()
        }

        assertFailsWith<AssertionError> {
            bytes.read<TestFoo>()
        }
    }

    @Test
    fun `should complain about wrong field count for class`() {
        val bytes = written.write().apply {
            // Keep track of field count, presently:
            this[indexOf(6.toByte())] = 5.toByte()
        }

        assertFailsWith<AssertionError> {
            bytes.read<TestFoo>()
        }
    }

    @Test
    fun `should complain about too few fields`() {
        val bytes = with(written.write()) {
            // Last field, "z" is an Int
            val truncated = copyOf(size - Int.SIZE_BYTES - Int.SIZE_BYTES)
            truncated[truncated.size - 1] = 0.toByte()
            truncated
        }

        assertFailsWith<AssertionError> {
            bytes.read<TestFoo>()
        }
    }

    @Test
    fun `should complain about wrong field type`() {
        val bytes = written.write().apply {
            this[indexOf('y'.toByte())] = 'z'.toByte()
        }

        assertFailsWith<AssertionError> {
            bytes.read<TestFoo>()
        }
    }

    @Test
    fun `should complain about extra bytes`() {
        val bytes = with(written.write()) {
            copyOf(size + 1)
        }

        assertFailsWith<AssertionError> {
            bytes.read<TestFoo>()
        }
    }
}

private data class TestFoo(
    val bint: BigInteger,
    val text: String,
    val byte: Byte,
    val bool: Boolean,
    val d: Double?,
    val z: Int,
)
