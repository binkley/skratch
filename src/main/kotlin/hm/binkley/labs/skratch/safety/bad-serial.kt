package hm.binkley.labs.skratch.safety

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.PrintStream
import java.io.Serializable
import java.lang.System.out

fun main() {
    val buf = ByteArrayOutputStream()
    val serialOut = ObjectOutputStream(buf)

    val safe = Foo("OK")
    println("SAFE -> $safe")

    serialOut.writeObject(safe)

    val data = buf.toByteArray()

    prettyPrint(out, data)

    val serialIn = ObjectInputStream(ByteArrayInputStream(data))
    val wonky = serialIn.readObject() as Foo

    println("WONKY -> $wonky")
}

private fun prettyPrint(out: PrintStream, data: ByteArray) {
    out.println(data.joinToString(" ", "[", "]") {
        "\\x%02X".format(it)
    })

    for (i in data.indices) when (data[i]) {
        'O'.toByte() -> data[i] = 'N'.toByte()
        'K'.toByte() -> data[i] = 'O'.toByte()
    }
}

data class Foo(val s: String) : Serializable {
    init {
        if (s != "OK") throw IllegalArgumentException("Not OK: $s")
    }

    companion object {
        private const val serialVersionUID: Long = 1
    }
}
