class Day09 : Day(9, "Disk Fragmenter") {

    private val line = parse()[0]

    override fun part1(): Any {
        val defrag = DiskDefrag(line)
        var checksum = 0L
        var index = 0
        while (defrag.hasNext()) {
            checksum += (index++ * defrag.next())
        }
        return checksum
    }

    override fun part2(): Any = TODO()

    private class DiskDefrag(private val diskMap: String) : Iterator<Int> {
        private val size = diskMap.length
        private var fileBlock = true

        // disk map pointers
        private var head = 0
        private var tail = size - 1

        // file ids that are currently being read, due to the alternating state of the string then the
        private var headFileId = 0
        private var tailFileId = size / 2

        // counters for how long left to spend on the current location
        private var current = -1
        private var currentTail = -1

        override fun hasNext(): Boolean {
            return head < tail
        }

        override fun next(): Int {
            return if (fileBlock) {
                generateNextFileBlock()
            } else {
                generateNextEmptySpaceBlock()
            }
        }

        private fun generateNextFileBlock(): Int {
            // current is file block size
            if (current == -1) {
                current = diskMap[head].digitToInt()
            }
            if (current == 0) {
                current = -1
                fileBlock = false
                headFileId++
                head++

                return next()
            }

            current--

            print(headFileId)

            return headFileId
        }

        private fun generateNextEmptySpaceBlock(): Int {
            // current is empty block size
            if (current == -1) {
                current = diskMap[head].digitToInt()
            }
            // current tail is file block size
            if (currentTail == -1) {
                currentTail = diskMap[tail].digitToInt()
            }
            if (current == 0 || currentTail == 0) {
                if (currentTail == 0) {
                    currentTail = -1
                    tail -= 2
                    tailFileId--
                }
                if (current == 0) {
                    current = -1
                    head++
                    fileBlock = true
                }

                return next()
            }

            currentTail--
            current--

            print(tailFileId)

            return tailFileId
        }


    }
}

fun main() {
    val day = Day09()
    println(day)
    println(day.part1())
//    println(Day09().process())
}
