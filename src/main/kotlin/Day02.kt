class Day02 : Day(2, "Red-Nosed Reports") {

    private val reports: List<Report> = parse().map { Report.parse(it) }

    override fun part1(): Any = reports.count { it.isSafe() }

    override fun part2(): Any = reports.count { it.isSafeWithTolerance() }

    private data class Report(val levels: List<Int>) {
        companion object {
            fun parse(line: String): Report {
                val elements = line.split(' ').map { Integer.parseInt(it) }
                return Report(elements)
            }
        }

        fun isSafe(): Boolean {
            if (levels.size <= 1) {
                return true
            }
            return when (val mode = Mode.determineMode(levels[0], levels[1])) {
                Mode.UNKNOWN -> false
                else -> levels.windowed(2).all { mode.isSafe(it[0], it[1]) }
            }
        }

        // can allow for one removal
        fun isSafeWithTolerance(): Boolean {
            if (levels.size <= 2) {
                return isSafe()
            }

            val mode: Mode = Mode.determineMode(levels[0], levels[1])
            // So we want to go through each window of 3 elements and check if a combination is right
            // Pressed for time (darn work) so just be simple and inefficient:
            // * if an invalid combination is found just alter the list and retry from scratch
            // * can only make one change so if invalid then we can just fail
            for (i in 0 until levels.size - 2) {
                if (mode.isSafe(levels[i], levels[i + 1]) && mode.isSafe(levels[i + 1], levels[i + 2])) {
                    continue
                }
                // try again without a given element
                fun retry(removalIndex: Int): Boolean {
                    val altered = levels.toMutableList()
                    altered.removeAt(removalIndex)
                    // just re-run the entire thing in case the mode needs to be recomputed
                    return Report(altered).isSafe()
                }
                return retry(i) || retry(i + 1) || retry(i + 2)
            }
            return true
        }


        enum class Mode {
            INCREASING {
                override fun isSafe(previous: Int, current: Int): Boolean = previous + 1 <= current && current <= previous + 3
            },
            DECREASING {
                override fun isSafe(previous: Int, current: Int): Boolean = previous - 3 <= current && current <= previous - 1
            },
            UNKNOWN {
                override fun isSafe(previous: Int, current: Int): Boolean = false
            };

            abstract fun isSafe(previous: Int, current: Int): Boolean

            companion object {
                fun determineMode(previous: Int, current: Int): Mode = when {
                    previous == current -> UNKNOWN
                    previous < current -> INCREASING
                    else -> DECREASING
                }
            }
        }
    }
}

fun main() = println(Day02().process())
