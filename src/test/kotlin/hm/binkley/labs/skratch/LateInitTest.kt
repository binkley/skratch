package hm.binkley.labs.skratch

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

internal class LateInitTest {
    @Test
    fun shouldLateInitVar() {
        assertNotNull(Node.third)
    }
}
