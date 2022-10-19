package hm.binkley.labs.skratch.jdk.reflection

fun main() {
    val bob = Bob(4)
    val strings = Nancy.stringsField.getInt(bob)
    println(bob)
    println(strings)
}

private data class Bob(
    val strings: Int
)

private object Nancy {
    @JvmStatic
    val stringsField = Bob::class.java.getDeclaredField("strings").apply {
        trySetAccessible()
    }
}
