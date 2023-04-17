package hm.binkley.labs.skratch.math.matrix

/** [Number] is already taken as a name. */
interface GeneralNumber<T, Norm> :
    Additive<T>,
    Multiplicative<T>,
    Scalable<T>,
    Conjugable<T>,
    Normed<T, Norm>
    where T : GeneralNumber<T, Norm>,
          Norm : GeneralNumber<Norm, Norm> {
    fun equivalent(other: GeneralNumber<*, *>): Boolean
}
