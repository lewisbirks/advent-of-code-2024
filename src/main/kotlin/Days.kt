import java.util.function.Supplier

fun main() {

    val days: List<Supplier<Day>> = listOf(Supplier { Day01() })

    days.map { it.get() }.forEach {
        """
            
            ╔════════════════════════════════════════════════
            ║ $it
            ╠════════════════════════════════════════════════
        """.trimIndent().println()
        var part = it.part1()
        printPart(part, "1")
        part = it.part2()
        printPart(part, "2")
        "╚════════════════════════════════════════════════".println()
    }
}

fun printPart(part: Any, num: String) {
    "║ Part $num:".println()
    if (part is String) {
        part.lines().map { "║   $it" }.forEach { it.println() }
    } else {
        "║   $part".println()
    }
}
