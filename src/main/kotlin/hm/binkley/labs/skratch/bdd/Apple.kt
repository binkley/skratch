package hm.binkley.labs.skratch.bdd

class Apple(val physicist: Newton) {
    fun falls(): Unit {
        physicist.thinking = true
    }
}