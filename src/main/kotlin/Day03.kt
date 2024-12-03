class Day03 : Day(3, "Mull It Over") {

    private val memory: String = parse().joinToString("\n")

    private fun scan(allowDisabling: Boolean = true): Long {
        var sum: Long = 0
        var i = 0

        val skipTerms = buildList {
            add("mul(")
            if (allowDisabling) {
                add("do()")
                add("don't()")
            }
        }

        fun findNext(search: String, start: Int): Int {
            val found = memory.indexOf(search, start)
            return if (found == -1) {
                memory.length
            } else {
                found
            }
        }

        while (i < memory.length) {
            when {
                memory.startsWith("mul(", i) -> {
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
                    val first: Int? = memory.substring(i, comma).toIntOrNull()
                    val second: Int? = memory.substring(comma + 1, end).toIntOrNull()
                    if (first == null || second == null) {
                        continue
                    }
                    sum += (first * second)
                    i = end + 1
                }
                allowDisabling && memory.startsWith("do()", i) -> i = findNext("mul(", i + 4)
                allowDisabling && memory.startsWith("don't()", i) -> i = findNext("do()", i + 7) + 4
                else -> i = memory.findAnyOf(skipTerms, i)?.first ?: memory.length
            }
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
