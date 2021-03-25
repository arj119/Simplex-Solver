import java.lang.StringBuilder

class Tableau(val indexSet: Array<Int>, val varNames: List<String>, vararg val rows: Row) {
    private val objectiveRow = rows[0].values
    private val visitedIndexSets = mutableSetOf<Array<Int>>()
    fun canTerminate(): Boolean {
        return objectiveRow.slice(0 .. rows[0].values.size - 2).all { it.toDouble() <= 0 }
    }

    fun isFeasible(): Boolean {
        return rows.slice(1 until rows.size).all { it.rhs().toDouble() >= 0 }
    }

    fun findPivot(bland: Boolean): Pair<Int, Int> {
        // find max index xp (column of pivot)
        var maxIndex = 0
        for (i in 1..objectiveRow.size - 2) {
            if(!bland && objectiveRow[i] > objectiveRow[maxIndex]) {
                maxIndex = i
            } else if(bland && objectiveRow[i] > Fraction(0)) {
                maxIndex = i
                break
            }
        }

        // find row of pivot
        var smallestRatioIndex = -1
        var smallestRatio = Fraction(Int.MAX_VALUE)
        for(i in 1 until rows.size) {
            val ratio = if(rows[i][maxIndex].toDouble() <= 0.0) {
                Fraction(Int.MAX_VALUE)
            } else {
                rows[i].rhs() / rows[i][maxIndex]
            }
            if(ratio < smallestRatio) {
                smallestRatio = ratio
                smallestRatioIndex = i
            }
        }

        // (row, col)
        return Pair(smallestRatioIndex, maxIndex)
    }

    fun swapAtPivot(pivot: Pair<Int, Int>) {
        val (r, c) = pivot

        // Swap out var name
        indexSet[r - 1] = c + 1

        // Gaussian elimination
        // normalise row
        rows[r] /= rows[r][c]

        for(i in rows.indices) {
            if(i == r) continue
            rows[i] -= rows[r] * rows[i][c]
        }
    }

    fun solve(bland: Boolean) {
        println("I = {${indexSet.joinToString(", ")}}")
        println(this)

//        if(visitedIndexSets.contains(indexSet)) {
//            println("Cycle found please use Bland's rule to solve")
//            return
//        }
//        visitedIndexSets.add(this.indexSet)

        // Check feasibilty
        if(!this.isFeasible()) {
            println("LP is infeasible")
            return
        }

        // Optimal solution found
        if(this.canTerminate()){
            println("Optimal BFS found as: ")
            val names: List<String> = indexSet.map { varNames[it - 1] }

            val rhs = rows.map { it.rhs() }
            println("(Z, ${names.joinToString(", ")}) = (${rhs.joinToString(", ")})")
            if(rhs.subList(1, rhs.size).any { it == Fraction(0) }) println("This is a degenerate solution")
            return
        } else {
            println("Terminating condition not met")
        }

        // Find pivot and do swap
        val pivot = this.findPivot(bland)
        println("Found pivot as: $pivot with value ${rows[pivot.first][pivot.second]}")
        println("Performing swap...")
        this.swapAtPivot(pivot)
        println()


        solve(bland)
    }

    override fun toString(): String {
        val out = StringBuilder("BV  |")
        out.append(varNames.joinToString("  ", prefix = "  ", postfix = "  | RHS\n"))
        val lineLen = out.length
        out.append("-".repeat(lineLen))
        out.append("\n")

        // Z
        val colSize = objectiveRow.size
        out.append("Z  |")
        out.append(objectiveRow.slice(0 .. colSize - 2).joinToString("  ", prefix = "  ", postfix = "  | "))
        out.append("${objectiveRow.last()} \n")

        var row = 1
        for(i in indexSet) {
            out.append("${varNames[i - 1]}  |")
            out.append(rows[row].values.slice(0 .. colSize - 2).joinToString("  ", prefix = "  ", postfix = "  | "))
            out.append("${rows[row].values.last()} \n")
            row++
        }

        return out.toString()
    }
}



