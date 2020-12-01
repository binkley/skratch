package hm.binkley.labs.skratch.math.matrix

class GeneralMatrix2x2<N : Number<N, Rational>>
    (a: N, b: N, c: N, d: N, private val elementCtor: (n: Long) -> N) :
    Matrix2x2<N, Rational, GeneralMatrix2x2<N>>(a, b, c, d) {
    override fun elementCtor(n: Long) = elementCtor.invoke(n)

    override fun matrixCtor(a: N, b: N, c: N, d: N) = GeneralMatrix2x2(a, b,
        c, d, elementCtor)
}
