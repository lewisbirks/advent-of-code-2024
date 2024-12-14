import Day10.TopographicMap.Topography
import Point.Direction
import Point.Direction.LEFT
import Point.Direction.UP
import Point.Location

class Day10 : Day(10, "Hoof It") {

    private val map: TopographicMap

    init {
        val lines = parse()
        val t: List<Topography> = lines.flatMapIndexed { y: Int, line: String -> line.mapIndexed { x, c -> Topography(Point(x, y), c.digitToInt()) } }
        map = TopographicMap(t, lines[0].length - 1, lines.size - 1)
    }

    override fun part1(): Any = map.trailheads().sumOf { map.score(it) }

    override fun part2(): Any = map.trailheads().sumOf {
        val score = map.rate(it)
        println("$it => $score")
        score
    }

    private class TopographicMap(private val topographies: List<Topography>, private val maxX: Int, private val maxY: Int) {
        private val lookupByHeight = buildMap<Int, Set<Point>> {
            val tmp = mutableMapOf<Int, MutableSet<Point>>()

            topographies.forEach { tmp.getOrPut(it.height) { mutableSetOf() }.add(it.position) }

            tmp.forEach { (i, p) -> put(i, buildSet { addAll(p) }) }
        }
        private val lookupByPosition = buildMap<Point, Int> {
            topographies.forEach { put(it.position, it.height) }
        }

        fun trailheads() = lookupByHeight[0]!!

        fun score(start: Point): Int {
            val foundEnds = mutableSetOf<Point>()
            val path = mutableListOf(Location(start, UP))
            val seen = mutableSetOf(path[0])
            // do depth first search
            while (path.isNotEmpty()) {
                val (location, direction) = path.removeLast()
                val currentHeight = lookupByPosition[location]!!
                val (nextLocation, nextDirection) = findNext(Topography(location, currentHeight), direction) ?: continue

                // add the next direction of the current position to check if it hasn't already been checked
                var next = Location(location, nextDirection)
                if (direction != LEFT && seen.add(next)) {
                    path.add(next)
                }

                // check if the next location is an ending
                if (isEnd(nextLocation)) {
                    foundEnds.add(nextLocation)
                    continue
                }

                // add the next position if it hasn't already been checked
                next = Location(nextLocation, UP)
                if (seen.add(next)) {
                    path.add(next)
                }
            }

            return foundEnds.size
        }

        fun rate(start: Point): Int {
            var found = 0
            val path = mutableListOf(Location(start, UP))
            val solvedPaths = mutableListOf<Set<Point>>()

            fun inExistingSolution(position: Point) = solvedPaths.count { it.contains(position) }

            // do depth first search
            while (path.isNotEmpty()) {
                val (location, direction) = path.removeLast()
                val currentHeight = lookupByPosition[location]!!
                val (nextLocation, nextDirection) = findNext(Topography(location, currentHeight), direction) ?: continue

                var next = Location(location, nextDirection)
                if (nextDirection != UP) {
                    path.add(next)
                }

                // check if the next location is an ending
                if (isEnd(nextLocation)) {
                    solvedPaths.add(buildSet {
                        path.forEach { add(it.position) }
                        add(nextLocation)
                    })
                    found++
                    continue
                }

                val inExistingSolutions = inExistingSolution(nextLocation)
                if (inExistingSolutions > 0) {
                    solvedPaths.add(buildSet {
                        path.forEach { add(it.position) }
                        add(nextLocation)
                    })
                    found += inExistingSolutions + 1
                    continue
                }
                // add the next position if it hasn't already been checked
                next = Location(nextLocation, UP)
                path.add(next)
            }

            return found
        }

        private fun findNext(current: Topography, direction: Direction): Pair<Point, Direction>? {
            var currentDirection = direction
            while (true) {
                val nextPosition = current.position.safeMove(currentDirection, maxX, maxY)
                val nextDirection = currentDirection.nextCardinalDirection()

                if (nextPosition != null) {
                    val next = Topography(nextPosition, lookupByPosition[nextPosition]!!)
                    if (current.isGradualIncline(next)) {
                        return Pair(nextPosition, nextDirection)
                    }
                }

                // exhausted all possible directions
                if (currentDirection == LEFT) {
                    return null
                }
                currentDirection = nextDirection
            }
        }

        private fun isEnd(point: Point) = lookupByHeight[9]!!.contains(point)

        data class Topography(val position: Point, val height: Int) {
            fun isGradualIncline(other: Topography): Boolean = other.height - this.height == 1
        }
    }
}

fun main() {
    val day = Day10()
    println(day)
    println(day.part1())
//    println(day.part2())
//    println(Day10().process())
}
