package hm.binkley.labs.skratch.bdd

data class Apple(
    val physicist: Newton
) {
    fun falls() {
        physicist.thinking = true
        physicist.eatingApple = false // INTENTIONALLY BROKEN
    }
}
