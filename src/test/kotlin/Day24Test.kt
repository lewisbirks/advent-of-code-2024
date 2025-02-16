import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day24Test {

    private val underTest = Day24()

    @Test
    fun part1() {
        assertEquals(2024L, underTest.part1())
    }

    @Test
    fun part2() {
        assertEquals(-1, underTest.part2())
    }
}
