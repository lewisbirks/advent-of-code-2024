fun main() {

    val days: List<() -> Day> = listOf({ Day01() }, { Day02() })

    days.map { it() }.forEach { it.process() }
}

