import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class Day14Test {

    private val underTest = Day14()

    @BeforeEach
    fun setUp() {
        underTest.maxX = 11
        underTest.maxY = 7
    }

    @Test
    fun part1() {
        assertEquals(12L, underTest.part1())
    }

    @Disabled
    @Test
    fun part2() {
        assertEquals(-1, underTest.part2())
    }
}
