import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day10Test {

    private val underTest = Day10()

    @Test
    fun part1() {
        assertEquals(36, underTest.part1())
    }

    @Test
    fun part2() {
        assertEquals(81, underTest.part2())
    }
}
