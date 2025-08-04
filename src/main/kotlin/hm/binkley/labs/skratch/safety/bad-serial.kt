package hm.binkley.labs.skratch.safety

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.PrintStream
import java.io.Serializable
import java.lang.System.out

fun main() {
    val safe = Foo("OK")
    println("SAFE -> $safe")

    val data = write(safe)

    prettyPrint(out, data)

    val wonky = read<Foo>(data)
    println("WONKY -> $wonky")
}

private fun write(o: Any): ByteArray {
    val buf = ByteArrayOutputStream()
    ObjectOutputStream(buf).writeObject(o)
    return buf.toByteArray()
}

@Suppress("SameParameterValue")
private fun prettyPrint(
    out: PrintStream,
    data: ByteArray
) {
    out.println(
        data.joinToString(" ", "[", "]") {
            "\\x%02X".format(it)
        }
    )

    for (i in data.indices) {
        when (data[i]) {
            'O'.code.toByte() -> data[i] = 'N'.code.toByte()
            'K'.code.toByte() -> data[i] = 'O'.code.toByte()
        }
    }
}

@Suppress("UNCHECKED_CAST")
private fun <T> read(data: ByteArray): T = ObjectInputStream(ByteArrayInputStream(data)).readObject() as T

private data class Foo(
    val s: String
) : Serializable {
    init {
        if (s != "OK") throw IllegalArgumentException("Not OK: $s")
    }

    companion object {
        private const val serialVersionUID: Long = 1
    }
}
