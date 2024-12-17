class Day14 : Day(14, "Restroom Redoubt") {

    private val initial: List<Robot> = parse().map { Robot.parse(it) }

    var maxX: Int = 101
    var maxY: Int = 103

    override fun part1(): Any {
        val map = Map(initial, maxX, maxY)
        map.moveRobots(100)

        return map.safety()
    }

    override fun part2(): Any {
//        val map = Map(initial, maxX, maxY)
//        var i = 0
//        fun printRobots() {
//            val entries = map.robots.map{ it.position }.groupBy { it }.map { it.key to it.value.size.toString() }.associate { it }
//
//            println("${"\n".repeat(maxY + 2)}Iteration: $i")
//            for (y in 0..<maxY) {
//                for (x in 0..<maxX) {
//                    val p = entries.getOrDefault(Point(x, y), ".")
//                    print(p)
//                }
//                println()
//            }
//        }
//
//        while (true) {
//            printRobots()
//            map.moveRobots(1)
//            i++
//            Thread.sleep(500)
//        }
//
//        return "nope"
        TODO()
    }


    private class Map(robots: List<Robot>, private val maxX: Int, private val maxY: Int) {
        val robots: List<Robot> = robots.map { it.copy() }

        fun moveRobots(times: Int) = this.robots.forEach { it.move(times, maxX, maxY) }

        fun safety(): Long {
            // `isValid` is inclusive so -1 here to ensure that things work as expected
            val maxX = this.maxX - 1
            val maxY = this.maxY - 1
            val leftHalfEnd = (maxX / 2) - 1
            val topHalfEnd = (maxY / 2) - 1
            val rightHalfStart = maxX - leftHalfEnd
            val bottomHalfStart = maxY - topHalfEnd

            var topLeft = 0L
            var topRight = 0L
            var bottomLeft = 0L
            var bottomRight = 0L

            // A | B
            // -----
            // C | D
            for (robot in robots) {
                val position = robot.position
                when {
                    // A
                    position.isValid(leftHalfEnd, topHalfEnd) -> topLeft++
                    // B
                    position.isValid(maxX, topHalfEnd, rightHalfStart, 0) -> topRight++
                    // C
                    position.isValid(leftHalfEnd, maxY, 0, bottomHalfStart) -> bottomLeft++
                    // D
                    position.isValid(maxX, maxY, rightHalfStart, bottomHalfStart) -> bottomRight++
                }
            }
            return topLeft * topRight * bottomLeft * bottomRight
        }
    }


    private class Robot private constructor(var position: Point, private val velocity: Velocity, private val id: Int){
        constructor(position: Point, velocity: Velocity): this(position, velocity, ++counter)

        fun copy(): Robot = Robot(position, velocity, id)

        fun move(times: Int, maxX: Int, maxY: Int) {
            val velocity = this.velocity * times
            var (x, y) = velocity.addTo(position)

            x %= maxX
            y %= maxY

            if (x < 0) x += maxX
            if (y < 0) y += maxY

            this.position = Point(x, y)
        }


        override fun toString(): String {
            return "$id => $position"
        }

        companion object {
            private var counter: Int = 0

            fun parse(line: String): Robot {
                val (unparsedP, unparsedV) = line.split(' ')
                fun process(to: String): Pair<Int, Int> {
                    val (x,y) = to.substring(2).split(',').map { it.toInt() }
                    return (x to y)
                }
                val position = process(unparsedP).let { (x,y) -> Point(x, y) }

                val velocity = process(unparsedV).let { (x,y) -> Velocity(x, y) }

                return Robot(position, velocity)
            }
        }
    }

    private data class Velocity(val x: Int, val y:Int) {
        fun addTo(position: Point): Point = Point(position.x + this.x, position.y + this.y)

        operator fun times(a: Int): Velocity = Velocity(x * a, y * a)
    }
}

fun main() {
    val day = Day14()
    println(day)
    println(day.part1())
    println(day.part2())
//    println(DayXX().process())
}
