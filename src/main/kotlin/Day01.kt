import kotlin.math.abs

class Day01 : Day(1, "Historian Hysteria") {

    private val left: MutableList<Int> = mutableListOf()
    private val right: MutableList<Int> = mutableListOf()

    init {
        parse().forEach {
            val split = it.split("   ")
            left.add(split[0].toInt())
            right.add(split[1].toInt())
        }

        left.sort()
        right.sort()
    }

    override fun part1(): Any = left.zip(right).sumOf { abs(it.first - it.second) }

    override fun part2(): Any {
        val frequencyMap: MutableMap<Int, Int> = mutableMapOf()

        fun find(value: Int): Int = frequencyMap.getOrPut(value) { right.count { search -> search == value } } * value

        return left.sumOf { find(it) }
    }
}

fun main() = println(Day01().process())
