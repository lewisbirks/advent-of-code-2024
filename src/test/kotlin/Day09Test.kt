import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day09Test {

    private val underTest = Day09()

    @Test
    fun part1() {
        assertEquals(1928L, underTest.part1())
    }

    @Test
    fun part2() {
        assertEquals(34, underTest.part2())
    }
}
