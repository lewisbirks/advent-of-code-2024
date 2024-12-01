abstract class Day(private val day: Int, private val name: String) {

    private val filename: String = "Day${String.format("%02d", day)}.txt"
    protected var input: List<String> = readInput(filename)

    abstract fun part1(): Any
    abstract fun part2(): Any

    protected open fun parse(): List<String> {
        return input
    }

    fun process() {
        fun buildPart(part: Any): String {
            return if (part is String) {
                part.lines().map { "║   $it" }.joinToString { "\n" }
            } else {
                "║   $part"
            }
        }

        sequenceOf(
            {
                """
                ╔════════════════════════════════════════════════
                ║ $this
                ╠════════════════════════════════════════════════
                ║ Part 1:
                """.trimIndent()
            },
            { buildPart(part1()) },
            { "║ Part 2" },
            { buildPart(part2()) },
            { "╚════════════════════════════════════════════════" })
            .forEach { it().println() }
    }


    override fun toString(): String {
        return "Day ${String.format("%02d", day)}: $name"
    }
}
