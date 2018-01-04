package hm.binkley.labs.skratch

class Node<T>(val value: T, val next: () -> Node<T>) {
    companion object {
        lateinit var third: Node<Int>

        val second = Node(2, { third })
        val first = Node(1, { second })

        init {
            third = Node(3, { first })
        }
    }
}
