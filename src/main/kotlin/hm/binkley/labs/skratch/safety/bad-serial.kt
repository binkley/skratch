package hm.binkley.labs.skratch.safety

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

fun main() {
    val buf = ByteArrayOutputStream()
    val serialOut = ObjectOutputStream(buf)

    val safe = Foo("OK")
    println("SAFE -> $safe")

    serialOut.writeObject(safe)

    val data = buf.toByteArray()
    val pretty = data.joinToString(" ", "[", "]") { "\\x%02X".format(it) }
    println("BYTES -> $pretty")

    for(i in data.indices) {
        when (data[i]) {
            'O'.toByte() -> {
                println("O@$i => N")
                data[i] = 'N'.toByte()
            }
            'K'.toByte() -> {
                println("K@$i => O")
                data[i] = 'O'.toByte()
            }
        }
    }

    val serialIn = ObjectInputStream(ByteArrayInputStream(data))
    val wonky = serialIn.readObject() as Foo

    println("WONKY -> $wonky")
}

data class Foo(val s: String) : Serializable {
    init {
        if (s != "OK") throw IllegalArgumentException("Not OK: $s")
    }

    companion object {
        private const val serialVersionUID: Long = 1
    }
}
