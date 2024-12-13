@file:Suppress("MagicNumber", "ReplacePrintlnWithLogging", "DuplicatedCode")

private const val DAY = "11"

private const val PART1_CHECK = 55312L
private const val PART2_CHECK = 65601038650482L

fun main() {
    fun blink(
        stone: Long,
        iteration: Int,
        cache: List<MutableMap<Long, Long>>,
    ): Long =
        if (iteration == 0) {
            1
        } else {
            val iterationCache = cache[iteration]
            iterationCache.getOrPut(stone) {
                val stoneStr = stone.toString()
                when {
                    stone == 0L -> blink(1, iteration - 1, cache)
                    stoneStr.length % 2 == 0 ->
                        blink(stoneStr.take(stoneStr.length / 2).toLong(), iteration - 1, cache) +
                            blink(stoneStr.takeLast(stoneStr.length / 2).toLong(), iteration - 1, cache)
                    else -> blink(stone * 2024, iteration - 1, cache)
                }
            }
        }

    fun part1(input: List<String>): Long {
        val cache = List(26) { mutableMapOf<Long, Long>() }
        return input
            .first()
            .split(' ')
            .map(String::toLong)
            .sumOf { stone ->
                blink(stone, 25, cache)
            }
    }

    fun part2(input: List<String>): Long {
        val cache = List(76) { mutableMapOf<Long, Long>() }
        return input
            .first()
            .split(' ')
            .map(String::toLong)
            .sumOf { stone ->
                blink(stone, 75, cache)
            }
    }

    println("Day $DAY")
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println("Part1 output: $it") } == PART1_CHECK)
    check(part2(testInput).also { println("Part2 output: $it") } == PART2_CHECK)

    val input = readInput("Day$DAY")
    println("Part1 final output: ${part1(input)}")
    // 197157
    println("Part2 final output: ${part2(input)}")
}
