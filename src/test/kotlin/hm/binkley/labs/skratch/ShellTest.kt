package hm.binkley.labs.skratch

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ShellTest {
    @Test
    fun shouldRunAny() = assertEquals(0, Shell("java", "-version").runAny())
}
