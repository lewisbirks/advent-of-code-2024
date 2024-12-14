import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Day11Test {

    private val underTest = Day11()

    @Test
    fun part1() {
        assertEquals(55312L, underTest.part1())
    }

    @Disabled
    @Test
    fun part2() {
        assertEquals(-1, underTest.part2())
    }
}
