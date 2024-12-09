import kotlin.math.log10
import kotlin.math.pow

class Day07 : Day(7, "Bridge Repair") {

    private val equations: List<Equation> = parse().map { Equation.from(it) }

    private val initialOperators = listOf(Operator.ADD, Operator.MULTIPLY)
    override fun part1(): Any = equations.filter { it.solve(initialOperators) }.sumOf { it.total }

    private val allOperators = listOf(Operator.ADD, Operator.MULTIPLY, Operator.CONCAT)
    override fun part2(): Any = equations.filter { it.solve(allOperators) }.sumOf { it.total }

    data class Equation(val total: Long, val numbers: List<Int>) {
        fun solve(operators: Collection<Operator>): Boolean = solve(operators, 1, numbers[0].toLong())

        private fun solve(operators: Collection<Operator>, index: Int, currentTotal: Long): Boolean {
            val isLast = index == numbers.size - 1
            for (operator in operators) {
                val newTotal = operator.apply(currentTotal, numbers[index])
                if (newTotal > total) {
                    continue
                }
                if (isLast) {
                    if (newTotal == total) {
                        return true
                    }
                } else {
                    if (solve(operators, index + 1, newTotal)) {
                        return true
                    }
                }
            }
            return false
        }

        override fun toString(): String = "$total: ${numbers.joinToString(" ")}"

        companion object {
            fun from(line: String): Equation {
                val sep = line.indexOf(':')
                val total = line.substring(0, sep).toLong()
                val numbers = line.substring(sep + 1).trim().split(' ').map { it.toInt() }
                return Equation(total, numbers)
            }
        }
    }

    enum class Operator {
        ADD {
            override fun apply(left: Number, right: Number): Long = left.toLong() + right.toLong()
            override fun toString(): String = "+"
        },
        MULTIPLY {
            override fun apply(left: Number, right: Number): Long = left.toLong() * right.toLong()
            override fun toString(): String = "*"
        },
        CONCAT {
            override fun apply(left: Number, right: Number): Long {
                val multiplier = log10(right.toDouble()).toInt() + 1
                val grown = left.toLong() * 10.0.pow(multiplier).toLong()
                return grown + right.toLong()
            }

            override fun toString(): String = "||"
        };

        abstract fun apply(left: Number, right: Number): Long
    }
}

fun main() = println(Day07().process())
