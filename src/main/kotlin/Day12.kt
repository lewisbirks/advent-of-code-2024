class Day12 : Day(12, "Garden Groups") {

    private val plants: MutableList<Plant>
    private val lookup: Map<Char, MutableSet<Plant>>
    private val maxX: Int
    private val maxY: Int
    private val regions: MutableList<Region> = mutableListOf()

    init {
        val lines = parse()
        plants = lines.flatMapIndexed { y, line -> line.mapIndexed { x, plant -> Plant(plant, Point(x, y)) } }.toMutableList()
        lookup = plants.fold(mutableMapOf()) { acc, plant ->
            acc.getOrPut(plant.plant) { mutableSetOf() }.add(plant)
            acc
        }
        maxX = lines[0].length - 1
        maxY = lines.size - 1
    }

    override fun part1(): Any = getRegions().sumOf { it.price() }

    override fun part2(): Any = getRegions().sumOf { it.bulkPrice() }

    private fun getRegions(): List<Region> {
        if (regions.isNotEmpty()) {
            return regions
        }

        fun findNext(current: Plant, plants: Set<Plant>): List<Plant> {
            return Point.Direction.cardinalDirections.mapNotNull { current.position.safeMove(it, maxX, maxY) }
                .map { Plant(current.plant, it) }
                .filter { it in plants }
        }

        while (plants.isNotEmpty()) {
            val plant = plants.removeFirst()
            val remainingOfType = lookup[plant.plant]!!

            val region = mutableSetOf(plant)
            val toCheck = mutableListOf(plant)
            val seen = mutableSetOf(plant)

            while (toCheck.isNotEmpty()) {
                val current = toCheck.removeFirst()
                remainingOfType.remove(current)
                plants.remove(current)

                val next = findNext(current, remainingOfType)
                for (n in next) {
                    if (seen.add(n)) {
                        toCheck.add(n)
                        region.add(n)
                    }
                }
            }

            regions.add(Region(region.map { it.position }.toSet()))
        }

        return regions
    }

    private data class Plant(val plant: Char, val position: Point) {
        override fun toString(): String = "$plant $position"
    }

    private data class Region(private val region: Set<Point>) {
        fun price() = region.size * perimeter()

        fun bulkPrice() = region.size * sides()

        private fun perimeter(): Int {
            return region.flatMap { plant -> Point.Direction.cardinalDirections.map { plant to it } }
                .count { (plant, direction) -> plant.move(direction) !in region }
        }

        private fun sides(): Int {
            TODO()
        }
    }
}

fun main() {
    val day = Day12()
    println(day)
    println(day.part1())
//    println(day.part2())
//    println(DayXX().process())
}
