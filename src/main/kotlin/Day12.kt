class Day12 : Day(12, "Garden Groups") {

    private val plants: MutableList<Plant>
    private val lookup: Map<Char, MutableSet<Point>>
    private val maxX: Int
    private val maxY: Int
    private val regions: Regions? = null

    init {
        val lines = parse()
        plants = lines.flatMapIndexed { y, line -> line.mapIndexed { x, plant -> Plant(plant, Point(x, y)) } }.toMutableList()
        lookup = plants.fold(mutableMapOf()) { acc, plant ->
            acc.getOrPut(plant.plant) { mutableSetOf() }.add(plant.position)
            acc
        }
        maxX = lines[0].length - 1
        maxY = lines.size - 1
    }

    override fun part1(): Any = getRegions().price()

    override fun part2(): Any = getRegions().bulkPrice()

    private fun getRegions(): Regions {
        if (regions != null) {
            return regions
        }

        fun findNext(current: Plant, plants: Set<Point>): List<Plant> = Point.Direction.cardinalDirections.mapNotNull { current.position.safeMove(it, maxX, maxY) }
            .filter { it in plants }
            .map { Plant(current.plant, it) }

        val regions: MutableList<Region> = mutableListOf()

        while (plants.isNotEmpty()) {
            val plant = plants.removeFirst()
            val remainingOfType = lookup[plant.plant]!!

            val region = mutableSetOf(plant)
            val toCheck = mutableListOf(plant)
            val seen = mutableSetOf(plant)

            while (toCheck.isNotEmpty()) {
                val current = toCheck.removeFirst()
                remainingOfType.remove(current.position)
                plants.remove(current)

                val next = findNext(current, remainingOfType)

                next.filter { seen.add(it) }.forEach {
                    toCheck.add(it)
                    region.add(it)
                }
            }

            regions.add(Region(region.map { it.position }.toSet()))
        }

        return Regions(regions)
    }

    private data class Plant(val plant: Char, val position: Point) {
        override fun toString(): String = "$plant $position"
    }

    private class Regions(private val regions: List<Region>) {

        fun price() = regions.sumOf { it.price() }

        fun bulkPrice(): Int {
            return -1
        }

    }

    private data class Region(private val region: Set<Point>) {
        val size = region.size

        fun price() = size * perimeter().size

        fun bulkPrice() = size * sides()

        private fun perimeter(): List<Pair<Point, Point.Direction>> {
            return region.flatMap { plant -> Point.Direction.cardinalDirections.map { plant to it } }
                .filter { (plant, direction) -> plant.move(direction) !in region }
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
