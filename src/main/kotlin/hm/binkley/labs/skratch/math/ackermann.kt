package hm.binkley.labs.skratch.math

fun main() {
    for (m in 1uL..3uL) // Stack overflow with 4
        for (n in 1uL..12uL) // Stack overflow with 13
            println("$m,$n -> ${ackermann(m, n)}")
}

private tailrec fun ackermann(m: ULong, n: ULong): ULong {
    return when {
        0uL == m -> n + 1uL
        0uL == n -> ackermann(m - 1uL, 1uL)
        // Jump out of tailrec because of the nested (parameter) call
        else -> ackermann(m - 1uL, ackermann0(m, n - 1uL))
    }
}

// Non-tailrec version
private fun ackermann0(m: ULong, n: ULong): ULong {
    return when {
        0uL == m -> n + 1uL
        0uL == n -> ackermann(m - 1uL, 1uL)
        else -> ackermann(m - 1uL, ackermann(m, n - 1uL))
    }
}
