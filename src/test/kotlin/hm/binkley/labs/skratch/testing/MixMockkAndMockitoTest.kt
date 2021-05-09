package hm.binkley.labs.skratch.testing

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class MixMockkAndMockitoTest {
    @Test
    fun withMockk() {
        val bob = mockk<Bob>()
        every { bob.nothing() } returns 1

        assertEquals(bob.nothing(), 1)
    }

    @Test
    fun withMockito() {
        val bob: Bob = mock()
        whenever(bob.nothing()).thenReturn(2)

        assertEquals(bob.nothing(), 2)
    }
}

interface Bob {
    fun nothing(): Int
}
