class Day04 : Day(4, "Ceres Search") {

    private val lines: List<String> = parse()
    private val xLocations: List<Point>
    private val aLocations: List<Point>
    private val maxY: Int = lines.size - 1
    private val maxX: Int = lines[0].length - 1

    init {
        val xLocations = mutableListOf<Point>()
        val aLocations = mutableListOf<Point>()
        lines.mapIndexed { y, line ->
            line.forEachIndexed { x, value ->
                if (value == 'X') {
                    xLocations.add(Point(x, y))
                } else if (value == 'A') {
                    aLocations.add(Point(x, y))
                }
            }
        }
        this.xLocations = xLocations
        this.aLocations = aLocations
    }

    override fun part1(): Any {
        val toFind = listOf('M', 'A', 'S')
        fun valid(direction: Direction, point: Point): Boolean {
            var current = point
            for (letter in toFind) {
                val neighbour = current.neighbour(direction, maxX, maxY) ?: return false
                if (lines[neighbour.y][neighbour.x] != letter) {
                    return false
                }
                current = neighbour
            }
            return true
        }

        return xLocations.sumOf { x -> Direction.entries.count { direction -> valid(direction, x) } }
    }

    override fun part2(): Any {
        fun valid(a: Point): Boolean {
            val topLeft = a.neighbour(Direction.UP_LEFT, maxX, maxY) ?: return false
            val topRight = a.neighbour(Direction.UP_RIGHT, maxX, maxY) ?: return false
            val bottomLeft = a.neighbour(Direction.DOWN_LEFT, maxX, maxY) ?: return false
            val bottomRight = a.neighbour(Direction.DOWN_RIGHT, maxX, maxY) ?: return false

            fun check(topLeftChar: Char, topRightChar: Char, bottomLeftChar: Char, bottomRightChar: Char): Boolean = lines[topLeft.y][topLeft.x] == topLeftChar
                    && lines[bottomLeft.y][bottomLeft.x] == bottomLeftChar
                    && lines[topRight.y][topRight.x] == topRightChar
                    && lines[bottomRight.y][bottomRight.x] == bottomRightChar

            // |-----|-----|
            // | M S | S M |
            // |  A  |  A  |
            // | M S | S M |
            // |-----|-----|
            // | M M | S S |
            // |  A  |  A  |
            // | S S | M M |
            // |-----|-----|
            return check('M', 'S', 'M', 'S') || check('S', 'M', 'S', 'M')
                    || check('M', 'M', 'S', 'S') || check('S', 'S', 'M', 'M')
        }

        return aLocations.count { valid(it) }
    }

    data class Point(val x: Int, val y: Int) {
        fun neighbour(direction: Direction, maxX: Int, maxY: Int): Point? {
            return move(direction).takeUnless { it.x < 0 || it.y < 0 || it.x > maxX || it.y > maxY }
        }

        private fun move(direction: Direction): Point {
            return when (direction) {
                Direction.LEFT -> Point(x - 1, y)
                Direction.RIGHT -> Point(x + 1, y)
                Direction.UP -> Point(x, y - 1)
                Direction.DOWN -> Point(x, y + 1)
                Direction.UP_LEFT -> Point(x - 1, y - 1)
                Direction.UP_RIGHT -> Point(x + 1, y - 1)
                Direction.DOWN_LEFT -> Point(x - 1, y + 1)
                Direction.DOWN_RIGHT -> Point(x + 1, y + 1)
            }
        }

        override fun toString(): String {
            return "(x=$x, y=$y)"
        }
    }

    enum class Direction {
        LEFT, RIGHT, UP, DOWN,
        UP_LEFT, UP_RIGHT,
        DOWN_LEFT, DOWN_RIGHT
    }
}

fun main() = println(Day04().process())
