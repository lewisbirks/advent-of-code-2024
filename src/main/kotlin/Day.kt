abstract class Day(private val day: Int, private val name: String) {

    private val filename: String = "Day${String.format("%02d", day)}.txt"
    private var input: List<String> = readInput(filename)

    abstract fun part1(): Any
    abstract fun part2(): Any

    protected open fun parse(): List<String> {
        return input
    }

    fun process() : Result = Result(this.toString(), part1(), part2())

    override fun toString(): String {
        return "Day ${String.format("%02d", day)}: $name"
    }

}

