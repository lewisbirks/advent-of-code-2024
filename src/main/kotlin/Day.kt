abstract class Day(private val day: Int, private val name: String) {

    private val filename: String = "Day${String.format("%02d", day)}.txt"
    protected var input: List<String> = readInput(filename)

    abstract fun part1(): Any
    abstract fun part2(): Any

    protected open fun parse(): List<String> {
        return input
    }

    fun process() {
        fun buildResult(part: Any): String = if (part is String) {
            part.lines().map { "║   $it" }.joinToString { "\n" }
        } else {
            "║   $part"
        }

        """
        ╔════════════════════════════════════════════════
        ║ $this
        ╠════════════════════════════════════════════════
        """.trimIndent().println()

        var result: Any = part1()

        """
            ║ Part 1:
            ${buildResult(result)}
        """.trimIndent().println()
        "╟────────────────────────────────────────────────".println()

        result = part2()

        """
            ║ Part 2:
            ${buildResult(result)}
        """.trimIndent().println()
        "╚════════════════════════════════════════════════".println()
    }


    override fun toString(): String {
        return "Day ${String.format("%02d", day)}: $name"
    }
}
