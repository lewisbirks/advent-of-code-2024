import Point.Direction
import Point.Direction.*

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
                val neighbour = current.safeMove(direction, maxX, maxY) ?: return false
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
            val topLeft = a.safeMove(UP_LEFT, maxX, maxY) ?: return false
            val topRight = a.safeMove(UP_RIGHT, maxX, maxY) ?: return false
            val bottomLeft = a.safeMove(DOWN_LEFT, maxX, maxY) ?: return false
            val bottomRight = a.safeMove(DOWN_RIGHT, maxX, maxY) ?: return false

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

}

fun main() = println(Day04().process())
