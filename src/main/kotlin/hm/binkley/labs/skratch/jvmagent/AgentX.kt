@file:JvmName("AgentX")

package hm.binkley.labs.skratch.jvmagent

import java.lang.instrument.Instrumentation

fun premain(arguments: String?, instrumentation: Instrumentation) {
    println("Hello from AgentX 'premain'!")
}

fun main(args: Array<String>) {
    println("Hello from AgentX 'main'!")
}
