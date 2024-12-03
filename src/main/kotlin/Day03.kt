private const val MULTIPLY = "mul("
private const val ENABLE = "do()"
private const val DISABLE = "don't()"

class Day03 : Day(3, "Mull It Over") {

    private val memory: String = parse().joinToString("")

    private fun scan(allowDisabling: Boolean = true): Long {
        var sum: Long = 0
        var i = 0

        val keyTerms = buildList {
            add(MULTIPLY)
            if (allowDisabling) {
                add(ENABLE)
                add(DISABLE)
            }
        }
        val enabledLookupTerms = listOf(MULTIPLY, DISABLE)
        val disabledLookupTerms = listOf(ENABLE)

        fun findNext(search: List<String>, start: Int): Int = memory.findAnyOf(search, start)?.first ?: memory.length

        while (i < memory.length) {
            if (memory.startsWith(MULTIPLY, i)) {
                i += 4
                val comma: Int = memory.indexOf(',', i)
                val end: Int = memory.indexOf(')', i)
                if (comma == -1 || end == -1) {
                    i = memory.length
                    continue
                }
                if (comma >= end) {
                    continue
                }
                val first = memory.substring(i, comma).toIntOrNull()
                val second = memory.substring(comma + 1, end).toIntOrNull()
                if (first == null || second == null) {
                    continue
                }
                sum += (first * second)
                i = end + 1
            } else if (allowDisabling && memory.startsWith(ENABLE, i)) {
                i = findNext(enabledLookupTerms, i + 4)
                continue
            } else if (allowDisabling && memory.startsWith(DISABLE, i)) {
                i = findNext(disabledLookupTerms, i + 7)
                continue
            }
            i = findNext(keyTerms, i)
        }
        return sum
    }

    /*
    Part 1 could have been (and was initially) done using regex, the pattern `mul\((?<first>\d+),(?<second>\d+)\)` was sufficient, using `first` and `second` to extract the required numbers;
    however I couldn't be bothered to work that out for part 2.
    Furthermore, part 2 is just an extension of part 1 and as such part 1 could just make use of the code from part 2 with the new parts disabled (hence the `allowDisabling`) param
     */
    override fun part1(): Any = scan(allowDisabling = false)

    override fun part2(): Any = scan(allowDisabling = true)
}

fun main() = println(Day03().process())
