@file:Suppress("MagicNumber", "ReplacePrintlnWithLogging")

private const val DAY = "04"

private const val PART1_CHECK = 18
private const val PART2_CHECK = 9

private val XMAS = "XMAS".chunked(1)

fun main() {
    fun matchesXmas(
        input: List<List<String>>,
        height: Int,
        width: Int,
        x: Int,
        y: Int,
        xDir: Int,
        yDir: Int,
    ): Boolean {
        return XMAS.indices.all { index ->
            val yOffset = y + yDir * index
            val xOffset = x + xDir * index
            if (yOffset !in 0..<height || xOffset !in 0..<width) {
                return@all false
            }
            val result = input[yOffset][xOffset] == XMAS[index]
            result
        }
    }

    fun matches2dimensional(
        input: List<List<String>>,
        height: Int,
        width: Int,
        x: Int,
        y: Int,
    ): Boolean {
        if (input[y][x] != "A") {
            return false
        }

        fun checkOffset(
            xDir: Int,
            yDir: Int,
            expected: String,
        ): Boolean {
            val yOffset = y + yDir
            val xOffset = x + xDir
            if (yOffset !in 0..<height || xOffset !in 0..<width) {
                return false
            }
            return input[yOffset][xOffset] == expected
        }
        var masMatch = 0
        if (checkOffset(-1, -1, "M") && checkOffset(1, 1, "S")) masMatch++
        if (checkOffset(1, -1, "M") && checkOffset(-1, 1, "S")) masMatch++
        if (checkOffset(1, 1, "M") && checkOffset(-1, -1, "S")) masMatch++
        if (checkOffset(-1, 1, "M") && checkOffset(1, -1, "S")) masMatch++
        return masMatch == 2
    }

    fun part1(rawInput: List<String>): Int {
        val input = rawInput.map { it.chunked(1) }
        val height = input.size
        val width = input.first().size

        var xmasCount = 0

        for (y in 0..<height) {
            for (x in 0..<width) {
                for (yDir in -1..1) {
                    for (xDir in -1..1) {
                        if (yDir != 0 || xDir != 0) {
                            val matches = matchesXmas(input, height, width, x, y, xDir, yDir)
                            if (matches) {
                                xmasCount += 1
                            }
                        }
                    }
                }
            }
        }

        return xmasCount
    }

    fun part2(rawInput: List<String>): Int {
        val input = rawInput.map { it.chunked(1) }
        val height = input.size
        val width = input.first().size

        var xmasCount = 0

        for (y in 0..<height) {
            for (x in 0..<width) {
                val matches = matches2dimensional(input, height, width, x, y)
                if (matches) {
                    xmasCount += 1
                }
            }
        }

        return xmasCount
    }

    println("Day $DAY")
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println("Part1 output: $it") } == PART1_CHECK)
    check(part2(testInput).also { println("Part2 output: $it") } == PART2_CHECK)

    val input = readInput("Day$DAY")
    println("Part1 final output: ${part1(input)}")
    println("Part2 final output: ${part2(input)}")
}
