data class Point(val x: Int, val y: Int) {
    fun safeMove(direction: Direction, maxX: Int, maxY: Int): Point? {
        return move(direction).takeIf { it.isValid(maxX, maxY) }
    }

    /**
     * Checks if the point is between the given bounds (inclusive)
     */
    fun isValid(maxX: Int, maxY: Int, minX: Int = 0, minY: Int = 0): Boolean {
        return this.x in minX..maxX && this.y in minY..maxY
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

    enum class Direction {
        LEFT, RIGHT, UP, DOWN,
        UP_LEFT, UP_RIGHT,
        DOWN_LEFT, DOWN_RIGHT;

        fun nextCardinalDirection() = when (this) {
            UP -> RIGHT
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
            else -> throw Exception("Unexpected direction $this")
        }
    }

    data class Location(val position: Point, val direction: Direction)
}
