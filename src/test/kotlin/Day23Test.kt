import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day23Test {

    private val underTest = Day23()

    @Test
    fun part1() {
        assertEquals(7, underTest.part1())
    }

    @Test
    fun part2() {
        assertEquals("co,de,ka,ta", underTest.part2())
    }
}
