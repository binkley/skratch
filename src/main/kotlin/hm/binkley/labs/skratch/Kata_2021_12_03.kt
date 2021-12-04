package hm.binkley.labs.skratch

fun main() {
    val node1 = listOf(1, 2, 5).toNode()
    val node2 = listOf(1, 3, 5).toNode()

    println(mergeTwoLists(node1, node2))
}

data class ListNode(var `val`: Int) {
    var next: ListNode? = null
}

fun mergeTwoLists(list1: ListNode?, list2: ListNode?): ListNode? {
    if (null == list1 && null == list2) return null
    if (null == list1) return list2!!.toList().sorted().toNode()
    if (null == list2) return list1.toList().sorted().toNode()
    return (list1.toList() + list2.toList()).sorted().toNode()
}

private fun ListNode.toList(): List<Int> {
    val list = mutableListOf<Int>()
    var node = this
    while (true) {
        list += node.`val`
        if (null == node.next) return list
        node = node.next!!
    }
}

private fun List<Int>.toNode(): ListNode {
    val head = ListNode(this[0])
    var curr = head
    for (idx in 1..(this.size - 1)) {
        curr.next = ListNode(this[idx])
        curr = curr.next!!
    }
    return head
}
