class Day05 : Day(5, "Print Queue") {

    private val rules: List<Rule>
    private val updates: List<Update>

    init {
        val rules = mutableListOf<Rule>()
        val updates = mutableListOf<Update>()
        val lines = parse()
        lines.forEach { line ->
            when {
                line.contains('|') -> rules.add(Rule.from(line))
                line.contains(',') -> updates.add(Update.from(line))
            }
        }
        this.rules = rules
        this.updates = updates
    }

    override fun part1(): Any = updates.filter { it.inOrder(rules) }.sumOf { it.middlePage }

    override fun part2(): Any = updates.filterNot { it.inOrder(rules) }.map { it.reorder(rules) }.sumOf { it.middlePage }

    private class Rule(val target: Int, val before: Int) {

        fun valid(update: Update): Boolean = update.pages.indexOf(target) < update.pages.indexOf(before)

        override fun toString(): String = "$target|$before"

        companion object {
            fun from(rule: String): Rule {
                val values = rule.split('|').map { it.toInt() }
                return Rule(values[0], values[1])
            }
        }
    }

    private class Update(val pages: List<Int>) {
        val middlePage = pages[pages.size / 2]

        override fun toString(): String = "$pages"

        fun inOrder(rules: List<Rule>): Boolean = rules.filter { it.target in pages && it.before in pages }
            .all { it.valid(this@Update) }

        fun reorder(rules: List<Rule>): Update {
            val applicableRules = rules.filter { it.target in pages && it.before in pages }

            val reordered = mutableListOf<Int>()

            pages.forEach { page ->
                if (reordered.isEmpty()) {
                    reordered.add(page)
                } else {
                    val index = applicableRules.filter { it.target == page && it.before in reordered }
                        .minOfOrNull { reordered.indexOf(it.before) } ?: reordered.size
                    reordered.add(index, page)
                }
            }

            return Update(reordered)
        }

        companion object {
            fun from(update: String): Update = Update(update.split(',').map { it.toInt() })
        }
    }
}

fun main() = println(Day05().process())
