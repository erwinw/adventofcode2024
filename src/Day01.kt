@file:Suppress("MagicNumber", "ReplacePrintlnWithLogging")

import kotlin.math.absoluteValue


private const val DAY = "01"

private const val PART1_CHECK = 11
private const val PART2_CHECK = 31

fun main() {
    fun part1(input: List<String>): Int {
        val parts = input.map { it.split(Regex("\\s+")) }
        val lefts = parts.map { it[0].toInt() }.sorted()
        val rights = parts.map { it[1].toInt() }.sorted()
        return lefts.zip(rights).sumOf { (l, r) -> (l - r).absoluteValue }
    }

    fun part2(input: List<String>): Int {
        val parts = input.map { it.split(Regex("\\s+")) }
        val lefts = parts.map { it[0].toInt() }
        val rights = parts.map { it[1].toInt() }.groupBy { it }.mapValues { it.value.size }
        return lefts.sumOf { l ->
            l * (rights[l] ?: 0)
        }
    }

    println("Day $DAY")
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println("Part1 output: $it") } == PART1_CHECK)
    check(part2(testInput).also { println("Part2 output: $it") } == PART2_CHECK)

    val input = readInput("Day$DAY")
    println("Part1 final output: ${part1(input)}")
    println("Part2 final output: ${part2(input)}")
}
