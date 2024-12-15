import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day12Test {

    private val underTest = Day12()

    @Test
    fun part1() {
        assertEquals(1930, underTest.part1())
    }

    @Test
    fun part2() {
        assertEquals(1206, underTest.part2())
    }
}
