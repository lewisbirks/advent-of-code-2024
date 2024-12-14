class Day11 : Day(11, "Plutonian Pebbles") {

    private val stones: List<Stone> = parse()[0].split(' ').map { Stone(it) }
    private val cache: MutableMap<Stone, List<Stone>> = mutableMapOf()

    override fun part1(): Any = blink(25)

    override fun part2(): Any = blink(75)

    private fun blink(times: Int): Long = (0..<times).fold(stones.associateBy({ it }, { 1L })) { state, _ -> blink(state) }.values.sum()

    private fun blink(state: Map<Stone, Long>): Map<Stone, Long> {
        fun getStones(stone: Stone): List<Stone> = cache.getOrPut(stone) { stone.change() }

        val resultantState = mutableMapOf<Stone, Long>()
        state.forEach { (stone, occurances) -> getStones(stone).forEach { resultantState.merge(it, occurances) { a, b -> a + b } } }
        return resultantState
    }

    private data class Stone(private val value: String) {
        fun change(): List<Stone> {
            if (value == "0") return listOf(Stone("1"))

            val length = value.length

            if (length % 2 == 1) return listOf(Stone((value.toLong() * 2024).toString()))

            val left = value.substring(0, length / 2)
            var right = value.substring(length / 2)
            if (right[0] == '0') {
                val start = right.indexOfFirst { it != '0' }
                right = if (start != -1) right.substring(start) else "0"
            }
            return listOf(Stone(left), Stone(right))
        }
    }
}

fun main() = println(Day11().process())
