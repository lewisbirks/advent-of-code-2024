import Point.Direction

class Day06 : Day(6, "Guard Gallivant") {

    private val obstacles: Set<Point>
    private val guard: Point
    private val maxX: Int
    private val maxY: Int

    init {
        val obstacles = mutableSetOf<Point>()
        var guard: Point? = null
        val lines = parse()
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c == '#') {
                    obstacles.add(Point(x, y))
                } else if (c == '^') {
                    guard = Point(x, y)
                }
            }
        }

        this.guard = guard!!
        this.obstacles = obstacles
        this.maxY = lines.size - 1
        this.maxX = lines[0].length - 1
    }

    override fun part1(): Any = buildPath(obstacles).map { it.position }.distinct().size

    private fun buildPath(obstacles: Set<Point>): List<Location> {
        val locations: MutableList<Location> = mutableListOf()
        val map = Map(guard, obstacles, maxX, maxY)

        while (true) {
            val location = map.step() ?: return locations
            locations.add(location)
        }
    }

    override fun part2(): Any {
        val locations = buildPath(obstacles)
        val checked = mutableSetOf<Point>()

        fun checkLocation(location: Location): Boolean {
            val blocker = location.position.safeMove(location.direction, maxX, maxY) ?: return false
            if (blocker in obstacles || blocker in checked) {
                return false
            }
            checked.add(blocker)

            val additionalObstacles = buildSet {
                addAll(obstacles)
                add(blocker)
            }
            // two pointer chasing
            val m1 = Map(guard, additionalObstacles, maxX, maxY)
            val m2 = Map(guard, additionalObstacles, maxX, maxY)

            var stepBoth = false
            while (true) {
                val p1 = m1.step() ?: return false
                if (stepBoth) {
                    val p2 = m2.step()
                    if (p1 == p2) {
                        return true
                    }
                }
                stepBoth = !stepBoth
            }
        }

        return locations.count { checkLocation(it) }
    }

    private data class Location(val position: Point, val direction: Direction)

    private class Map(private var guard: Point, private val obstacles: Set<Point>, private val maxX: Int, private val maxY: Int) {
        private var direction: Direction = Direction.UP

        fun step(): Location? {
            val tmp = guard.safeMove(direction, maxX, maxY) ?: return null
            if (tmp in obstacles) {
                direction = nextDirection(direction)
            } else {
                guard = tmp
            }
            return Location(guard, direction)
        }
    }

    companion object {
        private fun nextDirection(movement: Direction) = when (movement) {
            Direction.UP -> Direction.RIGHT
            Direction.RIGHT -> Direction.DOWN
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.UP
            else -> throw Exception("Unexpected direction $movement")
        }
    }
}

fun main() = println(Day06().process())
