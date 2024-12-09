import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day08Test {

    private val underTest = Day08()

    @Test
    fun part1() {
        assertEquals(14, underTest.part1())
    }

    @Test
    fun part2() {
        assertEquals(34, underTest.part2())
    }
}
