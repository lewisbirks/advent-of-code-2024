class Day23 : Day(23, "LAN Party") {

    private val connections: Set<Connection> = parse().map { it.split("-") }.map { Connection(it[0], it[1]) }.toSet()
    private val lookup: Map<String, List<Connection>> = buildMap {
        fun add(c: Connection, extractor: (Connection) -> String) {
            val comp = extractor.invoke(c)
            val bucket = this[comp]?.toMutableList() ?: mutableListOf()
            bucket.add(c)
            this[comp] = bucket
        }
        connections.forEach { c ->
            add(c) { it.comp1 }
            add(c) { it.comp2 }
        }
    }

    override fun part1(): Any {
        val ts = connections.filter { it.aCompStartsWith('t') }

        val found = mutableSetOf<Set<Connection>>()
        data class Possible(val confirmed: Connection, val possible: Connection)
        ts.forEach { t ->
            // co,de,ta
            // de-co, ta-co, de-ta
            // de-co, co-ta, de-ta
            lookup[t.comp1]!!.filter { it != t }
                .map {
                    val other = if (it.comp1 == t.comp1) it.comp2 else it.comp1
                    val toFind = Connection(t.comp2, other)
                    Possible(it, toFind)
                }
                .filter { it.possible in connections }
                .forEach { found.add(setOf(t, it.confirmed, it.possible)) }
        }
        return found.size
    }

    override fun part2(): Any = TODO()

    data class Connection(val comp1: String, val comp2: String) {
        fun aCompStartsWith(c: Char): Boolean = comp1[0] == c || comp2[0] == c

        fun contains(comp: String): Boolean = comp1 == comp || comp2 == comp

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || other !is Connection) return false
            return (comp1 == other.comp1 && comp2 == other.comp2) || (comp1 == other.comp2 && comp2 == other.comp1)
        }

        override fun hashCode(): Int {
            return listOf(comp1, comp2).sorted().hashCode()
        }

        override fun toString(): String = "$comp1-$comp2"
    }
}

fun main() {
    val day = Day23()
    println(day)
    println(day.part1())
//    println(day.part2())
//    println(DayXX().process())
}
