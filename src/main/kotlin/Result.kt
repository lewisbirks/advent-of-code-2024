class Result (private val name: String, private val part1: Any, private val part2: Any) {
    private fun formatResult(part: Any): String = if (part is String) {
        part.lines().joinToString("\n") { "║   $it" }
    } else {
        "║   $part"
    }

    override fun toString(): String {
        return """
            ╔════════════════════════════════════════════════
            ║ $name
            ╠════════════════════════════════════════════════
            ║ Part 1:
            ${formatResult(part1)}
            ╟────────────────────────────────────────────────
            ║ Part 2:
            ${formatResult(part2)}
            ╚════════════════════════════════════════════════
            """.trimIndent()
    }
}
