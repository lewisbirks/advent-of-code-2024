class Day08 : Day(8, "Resonant Collinearity") {

    private val allAntennas: List<Antennas>
    private val maxX: Int
    private val maxY: Int

    init {
        val lines = parse()
        val lookup = mutableMapOf<Char, Antennas>()
        lines.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                if (c.isLetterOrDigit()) {
                    lookup.getOrPut(c) { Antennas(c) }.add(Antenna(Point(x, y)))
                }
            }
        }
        allAntennas = buildList { addAll(lookup.values) }
        maxX = lines[0].length - 1
        maxY = lines[0].length - 1
    }

    override fun part1(): Any = allAntennas.flatMap { it.getAntinodes(maxX, maxY) }.distinct().count()

    override fun part2(): Any = allAntennas.flatMap { it.getAntinodes(maxX, maxY, true) }.distinct().count()

    private data class Antenna(val location: Point) {
        fun generateAntinodes(other: Antenna): Sequence<Point> {
            val diffX = other.location.x - this.location.x
            val diffY = other.location.y - this.location.y
            return generateSequence(Point(location.x, location.y)) { Point(it.x + diffX, it.y + diffY) }
        }

        override fun toString(): String = "$location"
    }

    private class Antennas(val symbol: Char) {
        private val antennas: MutableList<Antenna> = mutableListOf()

        fun add(antenna: Antenna) = antennas.add(antenna)

        fun getAntinodes(maxX: Int, maxY: Int, resonance: Boolean = false): List<Point> {
            fun antinodeGenerator(antenna: Antenna, other: Antenna): Sequence<Point> {
                var antinodes = antenna.generateAntinodes(other)

                if (!resonance) {
                    antinodes = antinodes.filterNot { it == antenna.location || it == other.location }.take(1)
                }
                return antinodes.takeWhile { it.isValid(maxX, maxY) }
            }

            if (antennas.size <= 1) {
                return emptyList()
            }

            return lazyCartesianProduct(antennas, antennas)
                .filterNot { it.first === it.second }
                .flatMap { antinodeGenerator(it.first, it.second) }
                .toList()
        }

        override fun toString(): String = "$symbol => $antennas"
    }
}

fun main() = println(Day08().process())
