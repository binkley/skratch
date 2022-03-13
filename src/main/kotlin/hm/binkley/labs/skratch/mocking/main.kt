package hm.binkley.labs.skratch.mocking

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import java.io.Console
import java.io.Reader

fun main() {
    println("REAL INTERACTIVE? -> ${isInteractive()}")
    mockkStatic(System::class) {
        println("MOCKING SYSTEM...")
        every { System.console() } returns doNothingConsole()
        println("MOCK INTERACTIVE? -> ${isInteractive()}")
    }

    mockkStatic(System::class) {
        println("MOCKING SYSTEM...")
        every { System.console() } returns eofConsole()
        val nextChar = System.console().reader().read().toChar()
        println("EOF -> '$nextChar'")
    }
}

private fun isInteractive() = null != System.console()

private fun doNothingConsole() = mockk<Console>()

private fun eofConsole(): Console {
    val eofConsole = mockk<Console>()
    val eofReader = mockk<Reader>()
    every { eofReader.read() } returns -1
    every { eofConsole.reader() } returns eofReader
    return eofConsole
}
