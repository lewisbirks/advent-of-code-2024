fun main() {

    val days: List<() -> Day> = listOf({ Day01() }, { Day02() }, { Day03() })

    days.map { it() }.forEach { println(it.process()) }
}

