package hm.binkley.labs.skratch.math.matrix

interface GeneralNumber<T, Norm> :
    Additive<T>,
    Multiplicative<T>,
    Scalable<T>,
    Normative<T, Norm>
        where T : GeneralNumber<T, Norm>,
              Norm : GeneralNumber<Norm, Norm> {
    val conj: T

    fun equivalent(other: GeneralNumber<*, *>): Boolean
}
