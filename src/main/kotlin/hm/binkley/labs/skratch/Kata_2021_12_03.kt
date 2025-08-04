package hm.binkley.labs.skratch

/** See [Kata](https://leetcode.com/problems/merge-two-sorted-lists/) */
fun main() {
    val node1 = listOf(1, 2, 5).toNode()
    val node2 = listOf(1, 3, 5).toNode()

    println(mergeTwoLists(node1, node2))
}

data class ListNode(
    var `val`: Int,
    var next: ListNode? = null
)

fun mergeTwoLists(
    list1: ListNode?,
    list2: ListNode?
): ListNode? {
    if (null == list1) return list2
    if (null == list2) return list1
    return (list1.toList() + list2.toList()).sorted().toNode()
}

private fun ListNode.toList(): List<Int> {
    val list = mutableListOf<Int>()
    var node = this
    while (true) {
        list += node.`val`
        val next = node.next ?: return list
        node = next
    }
}

private fun List<Int>.toNode(): ListNode {
    val head = ListNode(first())
    var curr = head
    for (value in drop(1)) {
        val next = ListNode(value)
        curr.next = next
        curr = next
    }
    return head
}
