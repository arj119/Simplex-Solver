data class Fraction(var num: Int, var den: Int = 1) {
    override fun toString(): String {
        if(num % den == 0) {
            return "${num / den}"
        }
        return "$num/$den"
    }

    operator fun plus(fraction: Fraction): Fraction {
        val denom = den * fraction.den
        val numer = num * fraction.den + fraction.num * den

        val res = Fraction(numer, denom)
        res.simplify()
        return res
    }

    operator fun minus(fraction: Fraction): Fraction {
        val denom = den * fraction.den
        val numer = num * fraction.den - fraction.num * den

        val res = Fraction(numer, denom)
        res.simplify()
        return res
    }

    private fun simplify() {
        val d = gcd(num, den)
        num /= d
        den /= d
    }

    private fun gcd(a: Int, b: Int): Int {
        if(b == 0) return a
        return gcd(b, a % b)
    }

    operator fun times(x: Fraction): Fraction {
        val out =  Fraction(num * x.num, den * x.den)
        out.simplify()
        return out
    }

    operator fun div(x: Fraction): Fraction {
        val out = Fraction(num * x.den, den * x.num)
        out.simplify()
        return out
    }

    operator fun compareTo(x: Fraction): Int {
        val res = this.toDouble().compareTo(x.toDouble())
        return res
    }

    fun toDouble(): Double = num.toDouble() / den.toDouble()
}