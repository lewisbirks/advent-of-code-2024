import kotlin.math.abs

class Day01 : Day(1, "Historian Hysteria") {

    private val left: MutableList<Int> = mutableListOf()
    private val right: MutableList<Int> = mutableListOf()

    init {
        parse().forEach {
            val split = it.split("   ")
            left.add(Integer.parseInt(split[0]))
            right.add(Integer.parseInt(split[1]))
        }
    }

    override fun part1(): Any {
        left.sort()
        right.sort()

        return left.zip(right).sumOf { abs(it.first - it.second) }
    }

    override fun part2(): Any {
        val frequencyMap: MutableMap<Int, Int> = mutableMapOf()

        fun find(value: Int) : Int {
            frequencyMap.computeIfAbsent(value) { _ -> right.count { search -> search == value } }
            return frequencyMap[value]!! * value
        }

        return left.sumOf { find(it) }
    }
}

fun main() {
    val day = Day01()

    day.println()

    "Part 1: ${day.part1()}".println()
    "Part 2: ${day.part2()}".println()
}
