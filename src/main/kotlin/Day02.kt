class Day02 : Day(2, "Red-Nosed Reports") {

    private val reports: List<Report> = parse().map { Report.parse(it) }

    override fun part1(): Any {
        return reports.count { it.isSafe() }
    }

    override fun part2(): Any {
        return reports.count { it.isSafeWithTolerance() }
    }

    data class Report(val levels: List<Int>) {
        companion object {
            fun parse(line: String): Report {
                val elements = line.split(' ').map { Integer.parseInt(it) }
                return Report(elements)
            }
        }

        fun isSafe() : Boolean {
            if (levels.size == 1) {
                return true
            }
            val mode = Mode.determineMode(levels[0], levels[1])
            if (mode == Mode.UNKNOWN) {
                return false
            }
            return levels.windowed(2).all { mode.isSafe(it[0], it[1]) }
        }

        // can allow for one removal
        fun isSafeWithTolerance(): Boolean {
            if (levels.size == 1) {
                return true
            }

            val mode = Mode.determineMode(levels[0], levels[1])
            val windows: List<List<Int>> = levels.windowed(3)

            // So we want to go through each window of 3 elements and check if a combination is right
            // Pressed for time (darn work) so just be simple and inefficient:
            // * if an invalid combination is found just alter the list and retry from scratch
            // * can only make one change so if invalid then we can just fail
            for ((index, window) in windows.withIndex()) {
                if (mode.isSafe(window[0], window[1]) && mode.isSafe(window[1], window[2])) {
                    continue
                }

                // try again without a given element
                fun retry(removalIndex: Int): Boolean {
                    val altered = levels.toMutableList()
                    altered.removeAt(removalIndex)
                    // just re-run the entire thing in case the mode needs to be recomputed
                    return Report(altered).isSafe()
                }
                return retry(index) || retry(index + 1) || retry(index + 2)
            }
            return true
        }


        enum class Mode {
            INCREASING {
                override fun isSafe(previous: Int, current: Int) : Boolean = previous + 1 <= current && current <= previous + 3
            },
            DECREASING {
                override fun isSafe(previous: Int, current: Int): Boolean = previous - 3 <= current && current <= previous - 1
            },
            UNKNOWN {
                override fun isSafe(previous: Int, current: Int): Boolean = false
            };

            abstract fun isSafe(previous: Int, current: Int) : Boolean

            companion object {
                fun determineMode(previous: Int, current: Int) : Mode {
                    return when {
                        previous == current -> UNKNOWN
                        previous < current -> INCREASING
                        else -> DECREASING
                    }
                }
            }
        }
    }
}

fun main() {
    Day02().process()
}
