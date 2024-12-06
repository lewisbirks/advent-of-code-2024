import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day06Test {

    private val underTest = Day06()

    @Test
    fun part1() {
        assertEquals(41, underTest.part1())
    }

    @Test
    fun part2() {
        assertEquals(6, underTest.part2())
    }
}
