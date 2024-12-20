import Point.Direction
import java.util.Objects

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
                    '#' -> walls.add(Point(x, y))
                    'O' -> boxes.add(Point(x, y))
                    '@' -> robot = Robot(Point(x, y))
                }
            }
        }

        this.robot = robot!!
        this.walls = walls.toSet()
        this.boxes = boxes.toSet()
        this.directions = directions.flatMap { line -> line.map { symbolToDirection[it]!! } }.toList()
    }

    override fun part1(): Any {
        val warehouse = Warehouse(walls.map { Wall(it) }, boxes.map { SimpleBox(it) }, robot)
        val robot = warehouse.robot

        directions.forEach { robot.move(it, warehouse) }

        return warehouse.sumBoxCoordinates()
    }

    override fun part2(): Any {
        val warehouse = Warehouse(
            walls.flatMap { sequenceOf(Point(it.x * 2, it.y), Point(it.x * 2 + 1, it.y)) }.map { Wall(it) },
            boxes.map { LargeBox(Point(it.x * 2, it.y), Point(it.x * 2 + 1, it.y)) },
            with(this.robot.location) { Robot(Point(this.x * 2, y)) }
        )
        val robot = warehouse.robot

        directions.forEach { robot.move(it, warehouse) }

        return warehouse.sumBoxCoordinates()
    }

    private data class Robot(var location: Point) {
        fun move(direction: Direction, warehouse: Warehouse) {
            val newLocation = location.move(direction)

            if (warehouse.shiftBoxes(newLocation, direction)) location = newLocation
        }
    }

    private class MoveResult(private val box: Box, private val mover: () -> Unit) {
        fun toRegister() = box.getNeighbours().toMutableList().plus(box).toList()

        fun apply() = mover.invoke()
    }

    private interface Neighbour {
        fun shift(direction: Direction, moves: MutableList<MoveResult>): Boolean
    }

    private object Empty : Neighbour {
        override fun shift(direction: Direction, moves: MutableList<MoveResult>): Boolean = true
    }

    private data class Wall(val location: Point) : Neighbour {
        override fun shift(direction: Direction, moves: MutableList<MoveResult>): Boolean = false
    }

    private interface Box : Neighbour {
        fun registerNeighbours(warehouse: Warehouse)
        fun getNeighbours(): List<Box>
        fun contains(possible: Point): Boolean
        fun coordinateValue(): Long
        fun getSymbol(point: Point): Char
    }

    private class SimpleBox(var location: Point) : Box {
        private lateinit var top: Neighbour
        private lateinit var bottom: Neighbour
        private lateinit var left: Neighbour
        private lateinit var right: Neighbour

        override fun registerNeighbours(warehouse: Warehouse) {
            fun assign(location: Point, assignor: (Neighbour) -> Unit) {
                val neighbour = warehouse.getWall(location) ?: warehouse.getBox(location, this) ?: Empty
                assignor(neighbour)
            }
            assign(location.move(Direction.UP)) { this.top = it }
            assign(location.move(Direction.DOWN)) { this.bottom = it }
            assign(location.move(Direction.LEFT)) { this.left = it }
            assign(location.move(Direction.RIGHT)) { this.right = it }
        }

        override fun getNeighbours(): List<Box> {
            return buildList {
                fun addBox(neighbour: Neighbour) {
                    if (neighbour is Box) add(neighbour)
                }
                addBox(top)
                addBox(bottom)
                addBox(left)
                addBox(right)
            }
        }

        override fun contains(possible: Point): Boolean = possible == location
        override fun coordinateValue(): Long = 100L * location.y + location.x
        override fun getSymbol(point: Point): Char = 'O'

        override fun shift(direction: Direction, moves: MutableList<MoveResult>): Boolean {
            val neighbour = when (direction) {
                Direction.LEFT -> left
                Direction.RIGHT -> right
                Direction.UP -> top
                Direction.DOWN -> bottom
                else -> throw Exception()
            }

            if (!neighbour.shift(direction, moves)) {
                return false
            }
            moves.add(MoveResult(this) { location = location.move(direction) })
            return true
        }

        override fun equals(other: Any?): Boolean = this === other || (other is SimpleBox && other.location == this.location)

        override fun hashCode(): Int = Objects.hash(location)

        override fun toString(): String = "$location"
    }

    private class LargeBox(var left: Point, var right: Point) : Box {

        private lateinit var leftNeighbour: Neighbour
        private lateinit var rightNeighbour: Neighbour
        private lateinit var topLeftNeighbour: Neighbour
        private lateinit var topRightNeighbour: Neighbour
        private lateinit var bottomLeftNeighbour: Neighbour
        private lateinit var bottomRightNeighbour: Neighbour

        override fun registerNeighbours(warehouse: Warehouse) {
            fun assign(location: Point, assignor: (Neighbour) -> Unit) {
                val neighbour = warehouse.getWall(location) ?: warehouse.getBox(location, this) ?: Empty
                assignor(neighbour)
            }

            assign(left.move(Direction.UP)) { this.topLeftNeighbour = it }
            assign(right.move(Direction.UP)) { this.topRightNeighbour = it }
            assign(left.move(Direction.LEFT)) { this.leftNeighbour = it }
            assign(right.move(Direction.RIGHT)) { this.rightNeighbour = it }
            assign(left.move(Direction.DOWN)) { this.bottomLeftNeighbour = it }
            assign(right.move(Direction.DOWN)) { this.bottomRightNeighbour = it }
        }

        override fun contains(possible: Point): Boolean = left == possible || right == possible
        override fun coordinateValue(): Long = 100L * left.y + left.x
        override fun getSymbol(point: Point): Char = if (left == point) '[' else ']'

        override fun shift(direction: Direction, moves: MutableList<MoveResult>): Boolean {
            if (direction == Direction.LEFT || direction == Direction.RIGHT) {
                val neighbour = if (direction == Direction.LEFT) leftNeighbour else rightNeighbour
                if (neighbour.shift(direction, moves)) {
                    moves.add(MoveResult(this) { move(direction) })
                    return true
                }
            } else {
                val left = if (direction == Direction.UP) topLeftNeighbour else bottomLeftNeighbour
                val right = if (direction == Direction.UP) topRightNeighbour else bottomRightNeighbour
                if (left.shift(direction, moves) && (left == right || right.shift(direction, moves))) {
                    moves.add(MoveResult(this) { move(direction) })
                    return true
                }
            }

            return false
        }

        private fun move(direction: Direction) = when (direction) {
            Direction.LEFT -> {
                right = left
                left = left.move(direction)
            }
            Direction.RIGHT -> {
                left = right
                right = right.move(direction)
            }
            Direction.UP, Direction.DOWN -> {
                left = left.move(direction)
                right = right.move(direction)
            }
            else -> {}
        }

        override fun getNeighbours(): List<Box> {
            return buildList {
                fun addBox(neighbour: Neighbour) {
                    if (neighbour is Box) add(neighbour)
                }
                addBox(topLeftNeighbour)
                addBox(topRightNeighbour)
                addBox(leftNeighbour)
                addBox(rightNeighbour)
                addBox(bottomLeftNeighbour)
                addBox(bottomRightNeighbour)
            }
        }

        override fun equals(other: Any?): Boolean = this === other || (other is LargeBox && other.left == this.left && other.right == this.right)

        override fun hashCode(): Int = Objects.hash(left, right)

        override fun toString(): String = "$left $right"
    }

    private class Warehouse(val walls: Set<Wall>, val boxes: List<Box>, val robot: Robot) {
        constructor (walls: Collection<Wall>, boxes: Collection<Box>, robot: Robot) : this(
            walls.toSet(),
            boxes.toList(),
            robot.copy()
        )

        init {
            boxes.forEach { it.registerNeighbours(this) }
        }

        fun getWall(possible: Point): Wall? = walls.find { it.location == possible }
        fun getBox(possible: Point, ignore: Box? = null): Box? = boxes.filter { it != ignore }.find { it.contains(possible) }

        fun sumBoxCoordinates(): Long = boxes.sumOf { it.coordinateValue() }

        fun shiftBoxes(initialLocation: Point, direction: Direction): Boolean {
            val box = getBox(initialLocation) ?: return getWall(initialLocation) == null
            val moves = mutableListOf<MoveResult>()

            if (!box.shift(direction, moves)) {
                return false
            }

            val oldNeighbours = moves.flatMap { it.toRegister() }
            moves.forEach { it.apply() }
            oldNeighbours.forEach { it.registerNeighbours(this) }
            val newNeighbours = moves.flatMap { it.toRegister() }.filter { it !in oldNeighbours }
            newNeighbours.forEach { it.registerNeighbours(this) }

            return true
        }

        override fun toString(): String {
            val maxX = walls.maxOf { it.location.x }
            val maxY = walls.maxOf { it.location.y }

            var warehouse = ""

            for (y in 0..maxY) {
                for (x in 0..maxX) {
                    val location = Point(x, y)
                    warehouse += when {
                        walls.any { it.location == location } -> '#'
                        boxes.any { it.contains(location) } -> boxes.first { it.contains(location) }.getSymbol(location)
                        robot.location == location -> '@'
                        else -> '.'
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
    println(day.part2())
//    println(DayXX().process())
}
