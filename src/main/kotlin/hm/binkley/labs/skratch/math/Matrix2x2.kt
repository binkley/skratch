package hm.binkley.labs.skratch.math

interface Matrix2x2<M : Matrix2x2<M>> {
    val rank: Int
        get() = 2
    val det: Ratio
    val trace: Ratio
    val transpose: M
    val inv: M
}
