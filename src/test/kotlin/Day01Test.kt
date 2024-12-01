import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day01Test {

    private val underTest: Day01 = Day01()

    @Test
    fun part1() {
        assertEquals(11, underTest.part1())
    }

    @Test
    fun part2() {
        assertEquals(31, underTest.part2())
    }
}