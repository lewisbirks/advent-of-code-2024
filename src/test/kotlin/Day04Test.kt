import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day04Test {

    private val underTest: Day04 = Day04()

    @Test
    fun part1() {
        assertEquals(18, underTest.part1())
    }

    @Test
    fun part2() {
        assertEquals(9, underTest.part2())
    }
}
