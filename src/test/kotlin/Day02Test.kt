import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day02Test {

    private val underTest: Day02 = Day02()

    @Test
    fun part1() {
        assertEquals(2, underTest.part1())
    }

    @Test
    fun part2() {
        assertEquals(4, underTest.part2())
    }
}
