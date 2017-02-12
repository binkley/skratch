package hm.binkley.labs.skratch

class Shell(val command: List<String>) {
    constructor(command: String) : this(command.split(' '))

    val shell = "/bin/sh"

    fun run() = ProcessBuilder(command.toMutableList().andAdd(0, shell)).
            start().
            waitFor()

    private fun <T> MutableList<T>.andAdd(index: Int, element: T)
            : MutableList<T> {
        this.add(index, element)
        return this
    }
}
