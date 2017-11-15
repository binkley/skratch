package hm.binkley.labs.skratch.math

interface Matrix2x2 {
    val rank: Int
    val det: Ratio
    val trace: Ratio
    val transpose: Matrix2x2
    val inv: Matrix2x2
}
