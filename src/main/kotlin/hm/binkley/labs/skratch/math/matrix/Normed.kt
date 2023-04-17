package hm.binkley.labs.skratch.math.matrix

interface Normed<T, Norm>
    where T : Normed<T, Norm>, Norm : Normed<Norm, Norm> {
    val absoluteValue: Norm
    val squareNorm: Norm
}
