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

    override fun part1(): Any {
        var movement: Direction = Direction.UP
        var guard: Point = this.guard
        val locations: MutableSet<Point> = mutableSetOf(guard)

        while (true) {
            val tmp = guard.safeMove(movement, maxX, maxY) ?: return locations.size
            if (tmp in obstacles) {
                movement = nextDirection(movement)
                continue
            }
            guard = tmp
            locations.add(guard)
        }
    }

    override fun part2(): Any = TODO()

    private fun nextDirection(movement: Direction) = when (movement) {
        Direction.UP -> Direction.RIGHT
        Direction.RIGHT -> Direction.DOWN
        Direction.DOWN -> Direction.LEFT
        Direction.LEFT -> Direction.UP
        else -> throw Exception("Unexpected direction $movement")
    }
}

fun main() {
    val day = Day06()
    println(day)
    println(day.part1())
//    println(Day06().process())
}
