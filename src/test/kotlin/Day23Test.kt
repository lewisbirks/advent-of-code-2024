import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Day23Test {

    private val underTest = Day23()

    @Test
    fun part1() {
        assertEquals(7, underTest.part1())
    }

    @Disabled
    @Test
    fun part2() {
        assertEquals(-1, underTest.part2())
    }
}
