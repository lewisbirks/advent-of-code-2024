import kotlin.math.min

class Day13 : Day(13, "Claw Contraption") {

    override fun part1(): Any = TODO()

    override fun part2(): Any = TODO()


    companion object {
        fun gcd(a: Int, b: Int): List<Pair<String, Int>> {
            // https://en.wikipedia.org/wiki/Euclidean_algorithm#Implementations
            fun euclidean(a: Int, b: Int): Int {
                var _a :Int
                var _b: Int
                if (a < b) {
                    _a = b
                    _b = a
                } else {
                    _a = a
                    _b = b
                }
                while (_b != 0) {
                    val tmp = _b
                    _b = _a % _b
                    _a = tmp
                }
                return _a
            }

            // https://en.wikipedia.org/wiki/Binary_GCD_algorithm
            fun binary(a: Int, b: Int): Int {
                var u = a
                var v = b
                // Base cases: gcd(n, 0) = gcd(0, n) = n
                if (u == 0) {
                    return b
                }
                if (v == 0) {
                    return a
                }

                // Using identities 2 and 3:
                // gcd(2ⁱ u, 2ʲ v) = 2ᵏ gcd(u, v) with u, v odd and k = min(i, j)
                // 2ᵏ is the greatest power of two that divides both 2ⁱ u and 2ʲ v
                val i = u.countTrailingZeroBits()
                val j = v.countTrailingZeroBits()
                u = u shr i
                v = v shr j
                val k = min(i, j)

                while (true) {
                    // Swap if necessary so u ≤ v
                    if (u > v) {
                        u = v.also { v = u }
                    }

                    // Identity 4: gcd(u, v) = gcd(u, v-u) as u ≤ v and u, v are both odd
                    v -= u
                    // v is now even

                    if (v == 0) {
                        // Identity 1: gcd(u, 0) = u
                        // The shift by k is necessary to add back the 2ᵏ factor that was removed before the loop
                        return u shl k
                    }

                    // Identity 3: gcd(u, 2ʲ v) = gcd(u, v) as u is odd
                    v = v shr v.countTrailingZeroBits()
                }
            }

            return listOf("Euclidean" to euclidean(a, b), "Binary" to binary(a, b))
        }
    }
}

fun main() {
    val day = Day13()
    println(day)
    println(Day13.gcd(22, 94))
    println(Day13.gcd(26, 67))
//    println(day.part1())
//    println(day.part2())
//    println(DayXX().process())
}
