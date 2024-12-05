import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day05Test {

    private val underTest = Day05()

    @Test
    fun part1() {
        assertEquals(143, underTest.part1())
    }

    @Test
    fun part2() {
        assertEquals(123, underTest.part2())
    }
}
