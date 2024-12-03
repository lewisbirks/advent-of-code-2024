import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class Day03Test {

    private val underTest: Day03 = Day03()

    @Test
    fun part1() {
        assertEquals(161L, underTest.part1())
    }

    @Test
    fun part2() {
        assertEquals(48L, underTest.part2())
    }
}
