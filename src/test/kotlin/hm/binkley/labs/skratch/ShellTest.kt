package hm.binkley.labs.skratch

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.io.IOException

internal class ShellTest {
    @Test
    fun shouldRunAnyWithSuccess()
            = assertEquals(0, Shell("java", "-version").runAny())

    @Test
    fun shouldRunAnyWithFailure() {
        assertThrows<IOException>(IOException::class.java, {
            Shell("no-such-program").runAny()
        })
    }
}
