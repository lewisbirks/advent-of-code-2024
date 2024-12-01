fun main() {

    val days: List<() -> Day> = listOf({ Day01() })

    days.map { it() }.forEach { it.process() }
}

