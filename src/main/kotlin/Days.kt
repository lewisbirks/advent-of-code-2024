fun main() {
    val days: List<() -> Day> = listOf(
        { Day01() }, { Day02() }, { Day03() }, { Day04() }, { Day05() }, { Day06() }, { Day07() },
        { Day08() }, { Day11() }
    )

    days.map { it() }.forEach { println(it.process()) }
}

