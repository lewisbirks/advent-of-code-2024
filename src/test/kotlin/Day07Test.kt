import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day07Test {

    private val underTest = Day07()

    @Test
    fun part1() {
        assertEquals(3749L, underTest.part1())
    }

    @Test
    fun part2() {
        assertEquals(11387L, underTest.part2())
    }
}
