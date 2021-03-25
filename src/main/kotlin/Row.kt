class Row(val values: Array<Fraction>) {
    operator fun minusAssign(row: Row) {
        for(i in row.values.indices) {
            this[i] -= row[i]
        }
    }

    private operator fun set(i: Int, value: Fraction) {
        values[i] = value
    }

    operator fun get(i: Int): Fraction = values[i]

    operator fun timesAssign(x: Fraction) {
        for(i in values.indices) {
            this[i] *= x
        }
    }

    operator fun times(x: Fraction): Row {
        val valuesCopy = values.copyOf()
        return Row(*valuesCopy.map {it * x}.toTypedArray())
    }

    operator fun divAssign(x: Fraction) {
        for(i in values.indices) {
            this[i] /= x
        }
    }

    fun rhs(): Fraction = values.last()

}