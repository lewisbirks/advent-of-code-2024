data class Point(val x: Int, val y: Int) {
    fun safeMove(direction: Direction, maxX: Int, maxY: Int): Point? {
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

    enum class Direction {
        LEFT, RIGHT, UP, DOWN,
        UP_LEFT, UP_RIGHT,
        DOWN_LEFT, DOWN_RIGHT
    }
}
