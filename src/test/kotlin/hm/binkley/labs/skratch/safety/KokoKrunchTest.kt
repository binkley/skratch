package hm.binkley.labs.skratch.safety

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class KokoKrunchTest {
    @Test
    fun `should round trip`() {
        val written = TestFoo(
            b = 3.toByte(),
            d = null,
            i = 13,
            s = "BOB",
        )
        val read = written.write().read<TestFoo>()

        assertEquals(written, read)
    }
}

private data class TestFoo(
    val s: String,
    val b: Byte,
    val i: Int,
    val d: Double?,
)
