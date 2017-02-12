package hm.binkley.labs.skratch

class Shell(val command: List<String>) {
    constructor(vararg command: String) : this(command.toList())

    fun runAny() = ProcessBuilder(command).
            inheritIO().
            start().
            waitFor()
}
