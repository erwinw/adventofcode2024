@file:Suppress("MagicNumber", "ReplacePrintlnWithLogging", "DuplicatedCode")

private const val DAY = "19"

private const val PART1_CHECK = 6
private const val PART2_CHECK = 16L

fun main() {
    fun part1(input: List<String>): Int {
        val towelPatterns = input.first().split(", ").sortedByDescending { it.length }

        val desiredDesigns = input.drop(2)

        val possibleCache = mutableMapOf<String, Boolean>()

        fun isPossible(design: String): Boolean {
            if (design.isEmpty()) {
                return true
            }

            possibleCache[design]?.let {
                return it
            }

            return towelPatterns
                .any { towelPattern ->
                    design.take(towelPattern.length) == towelPattern && isPossible(design.drop(towelPattern.length))
                }.also {
                    possibleCache[design] = it
                }
        }

        return desiredDesigns
            .map(::isPossible)
            .count { it }
    }

    fun part2(input: List<String>): Long {
        val towelPatterns = input.first().split(", ").sortedByDescending { it.length }

        val desiredDesigns = input.drop(2)

        val possibleCache = mutableMapOf<String, Long>()

        fun countPossible(design: String): Long {
            if (design.isEmpty()) {
                return 1
            }

            possibleCache[design]?.let {
                return it
            }

            return towelPatterns
                .sumOf { towelPattern ->
                    if (design.take(towelPattern.length) == towelPattern) {
                        countPossible(design.drop(towelPattern.length))
                    } else {
                        0L
                    }
                }.also {
                    possibleCache[design] = it
                }
        }

        return desiredDesigns.sumOf(::countPossible)
    }

    println("Day $DAY")
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println("Part1 output: $it") } == PART1_CHECK)
    check(part2(testInput).also { println("Part2 output: $it") } == PART2_CHECK)

    val input = readInput("Day$DAY")
    println("Part1 final output: ${part1(input)}")
    println("Part2 final output: ${part2(input)}")
}
