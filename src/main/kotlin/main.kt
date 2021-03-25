import java.util.*
import kotlin.system.exitProcess

object Main {

    private var tableau: Tableau = Tableau(
        arrayOf(3, 4, 5), listOf("x1", "x2", "x3", "x4", "x5"),
        Row(arrayOf(Fraction(4), Fraction(3), Fraction(0), Fraction(0), Fraction(0), Fraction(0))),
        Row(arrayOf(Fraction(3), Fraction(4), Fraction(1), Fraction(0), Fraction(0), Fraction(12))),
        Row(arrayOf(Fraction(3), Fraction(3), Fraction(0), Fraction(1), Fraction(0), Fraction(10))),
        Row(arrayOf(Fraction(4), Fraction(2), Fraction(0), Fraction(0), Fraction(1), Fraction(8)))
    )
    private val scanner = Scanner(System.`in`)

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            while (true) {
                print(">>> ")
                val line = scanner.nextLine()
                processCommand(line)
            }
        } catch (e: IllegalStateException) { // System.in has been closed
            println("System.in was closed; exiting")
        } catch (e: NoSuchElementException) {
            println("System.in was closed; exiting")
        }
    }

    fun processCommand(command: String) {
        val tokens = command.split(" ")
        when (tokens[0]) {
            "load" -> loadTableau()
            "solve" -> tableau.solve(false)
            "bland-solve" -> tableau.solve(true)
            "exit"-> exitProcess(0)
            else -> println("Command ${tokens[0]} not supported")
        }
    }

    fun loadTableau() {
        // decision variables
        print("Please enter decision variables (space separated not including Z): ")
        var line = scanner.nextLine()
        val decisionVariables = line.split(" ")

        // rows
        val rows = mutableListOf<Row>()
        var row = 0
        while (true) {
            println("Please enter coefficients for row omitting Z columns ${if(row == 0) "Z" else row} it should be of size " +
                    "${decisionVariables.size + 1} with RHS included at the end(space separated fractions are like 4/5) if done enter done: ")
            print(">>> ")
            line = scanner.nextLine()
            if(line.contains("done")) break

            val coeffs = line.split(" ")
            if(coeffs.size != decisionVariables.size + 1) {
                println("invalid size try again")
                continue
            }
            rows.add(parseRow(coeffs))
            row++
        }

        var indices: List<Int>
        // initial index set
        while(true) {
            println("Please enter index set, it should be of size ${rows.size - 1} (space separated): ")
            line = scanner.nextLine()
            indices = line.split(" ").map { it.toInt() }
            if (indices.size == rows.size - 1) break
            println("invalid size try again")
        }

        this.tableau = Tableau(indexSet = indices.toTypedArray(), varNames = decisionVariables, *rows.toTypedArray())
        println("Tableau loaded: ")
        println(this.tableau)
    }

    fun parseRow(coeffs: List<String>): Row {
        val fractions: List<Fraction> = coeffs.map {
            val s = it.trim()
            if(s.contains('/')) {
                val nums = s.split("/")
                Fraction(nums[0].toInt(), nums[1].toInt())
            } else {
                Fraction(s.toInt())
            }
        }

        return Row(fractions.toTypedArray())
    }

}