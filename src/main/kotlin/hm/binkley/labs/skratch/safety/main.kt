package hm.binkley.labs.skratch.safety

import sun.misc.Unsafe
import java.nio.ByteBuffer

fun main() {
    val cereal = Cereal(
        byte = 0b01010101,
        d = null,
        int = 3,
        string = "THREE",
    )
    val bytes = cereal.write()

    println("WRITE -> $cereal")
    println("BYTES -> ${bytes.pretty()}")
    println("READ -> ${bytes.read<Cereal>()}")
}

private fun ByteArray.dump() {
    val unsafe = Unsafe::class.java.getDeclaredField("theUnsafe").apply {
        isAccessible = true
    }.get(null) as Unsafe

    val buf = ByteBuffer.wrap(this)

    var len = buf.int
    var tmp = ByteArray(len)
    buf.get(tmp)
    buf.get()
    val className = String(tmp)

    val clazz = Class.forName(className)
    val instance = unsafe.allocateInstance(clazz)

    println("CLASS NAME -> $className -> BLANK: $instance")

    buf.int
    val count = buf.int
    buf.get()
    println("FIELD COUNT -> $count")

    // TODO: Validate field count the same

    for (n in 1..count) {
        println("FIELD #$n")

        len = buf.int
        tmp = ByteArray(len)
        buf.get(tmp)
        buf.get()
        val fieldName = String(tmp)
        val field = clazz.getDeclaredField(fieldName).apply {
            isAccessible = true
        }

        println("FIELD NAME -> $fieldName -> $field")

        len = buf.int
        tmp = ByteArray(len)
        buf.get(tmp)
        buf.get()
        val fieldClassName = String(tmp)
        println("FIELD CLASS NAME -> $fieldClassName")

        len = buf.int
        val value: Any?
        if (-1 == len) {
            value = null
        } else {
            value = when (fieldClassName) {
                Boolean::class.java.name -> 0.toByte() != buf.get()
                Byte::class.java.name -> buf.get()
                Char::class.java.name -> buf.char
                Double::class.java.name -> buf.double
                Float::class.java.name -> buf.float
                Int::class.java.name -> buf.int
                Long::class.java.name -> buf.long
                String::class.java.name -> {
                    tmp = ByteArray(len)
                    buf.get(tmp)
                    String(tmp)
                }
                else -> TODO("All the rest")
            }
        }
        buf.get()
        println("FIELD VALUE -> $value")

        field.set(instance, value)
    }

    buf.get()

    println("INSTANCE -> $instance")
}
