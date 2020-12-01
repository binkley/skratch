@file:JvmName("AgentX")

package hm.binkley.labs.skratch.jvmagent

import java.lang.instrument.Instrumentation

fun premain(arguments: String?, instrumentation: Instrumentation) {
    println("Hello from AgentX 'premain'!")
    println("ARGUMENTS -> $arguments")
    println("INSTRUMENTATION -> $instrumentation")
}

fun main() {
    println("Hello from AgentX 'main'!")
}
