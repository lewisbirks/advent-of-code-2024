import Point.Direction

private const val WALL = '#'
private const val BOX = 'O'
private const val ROBOT = '@'

class Day15 : Day(15, "Warehouse Woes") {
    private var boxes: Set<Point>
    private var walls: Set<Point>
    private val robot: Robot
    private val directions: List<Direction>

    private val directionToSymbol = mapOf(Direction.LEFT to '<', Direction.UP to '^', Direction.RIGHT to '>', Direction.DOWN to 'v')
    private val symbolToDirection = mapOf('<' to Direction.LEFT, '^' to Direction.UP, '>' to Direction.RIGHT, 'v' to Direction.DOWN)

    init {
        val lines = parse()
        val sep = lines.indexOf("")
        val warehouse = lines.subList(0, sep)
        val directions = lines.subList(sep + 1, lines.size)

        val walls = mutableSetOf<Point>()
        val boxes = mutableSetOf<Point>()
        var robot: Robot? = null

        warehouse.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                when (c) {
                    WALL -> walls.add(Point(x, y))
                    BOX -> boxes.add(Point(x, y))
                    ROBOT -> robot = Robot(Point(x, y))
                }
            }
        }

        this.robot = robot!!
        this.walls = walls.toSet()
        this.boxes = boxes.toSet()
        this.directions = directions.flatMap { line -> line.map { symbolToDirection[it]!! } }.toList()
    }

    override fun part1(): Any {
        val warehouse = Warehouse(walls, boxes, robot)
        val robot = warehouse.robot

        directions.forEach { robot.move(it, warehouse) }

        return warehouse.sumBoxCoordinates()
    }

    override fun part2(): Any = TODO()

    private class Robot(location: Point) {
        var location = location
            private set

        fun move(direction: Direction, warehouse: Warehouse) {
            val newLocation = location.move(direction)

            if (newLocation in warehouse.walls) {
                return
            }
            if (warehouse.isBox(newLocation) && !warehouse.shiftBoxes(newLocation, direction)) {
                return
            }
            location = newLocation
        }

        fun copy() = Robot(location)

        override fun equals(other: Any?): Boolean = this === other || (other is Robot && location == other.location)

        override fun hashCode(): Int = location.hashCode()
    }

    private class Warehouse(walls: Set<Point>, boxes: Set<Point>, robot: Robot) {
        val walls: Set<Point> = walls.toSet()
        val boxes: MutableSet<Point> = boxes.toMutableSet()
        val robot: Robot = robot.copy()

        fun sumBoxCoordinates(): Long = boxes.sumOf { 100L * it.y + it.x }

        /**
         * @return false if the boxes could not be shifted in the given direction
         */
        fun shiftBoxes(initialBox: Point, direction: Direction): Boolean {
            var lastBox = initialBox.move(direction)

            while (lastBox in boxes) {
                lastBox = lastBox.move(direction)
            }

            if (lastBox in walls) {
                return false
            }

            boxes.remove(initialBox)
            boxes.add(lastBox)

            return true
        }

        fun isBox(location: Point): Boolean = location in boxes

        override fun toString(): String {
            val maxX = walls.maxOf { it.x }
            val maxY = walls.maxOf { it.y }

            var warehouse = ""

            for (y in 0..maxY) {
                for (x in 0..maxX) {
                    val current = Point(x, y)
                    when {
                        current in walls -> warehouse += WALL
                        current in boxes -> warehouse += BOX
                        robot.location == current -> warehouse += ROBOT
                        else -> warehouse += "."
                    }
                }
                warehouse += "\n"
            }

            return warehouse
        }
    }
}

fun main() {
    val day = Day15()
    println(day)
    println(day.part1())
//    println(day.part2())
//    println(DayXX().process())
}
