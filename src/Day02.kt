@file:Suppress("MagicNumber", "ReplacePrintlnWithLogging")

import kotlin.math.absoluteValue
import kotlin.math.sign


private const val DAY = "02"

private const val PART1_CHECK = 2
private const val PART2_CHECK = 4

private fun isPairSafe(numbers: List<Int>, i: Int, increasing: Boolean): Boolean {
    val isMonotonic = (numbers[i] - numbers[i - 1]).sign == if (increasing) 1 else -1
    val safeDifference = (numbers[i] - numbers[i - 1]).absoluteValue in (1..3)
    println("Index $i: ${numbers[i - 1]} - ${numbers[i]}; monotic: $isMonotonic; safe: $safeDifference")
    return isMonotonic && safeDifference
}

fun main() {
    fun part1(input: List<String>): Int =
        input.count { line ->
            val numbers = line.split(" ").map(String::toInt)
            val increasing = when ((numbers[1] - numbers[0]).sign) {
                1 -> true
                -1 -> false
                0 -> return@count false
                else -> error("Unexpected sign result for ${numbers[0]} - ${numbers[1]}")
            }

            (1..<numbers.size).all { i ->
                isPairSafe(numbers, i, increasing)
            }
        }

    fun part2(input: List<String>): Int =
        input.count { line ->
            println("\nLine: $line")
            val numbers = line.split(" ").map(String::toInt)
            val increasing = when ((numbers[1] - numbers[0]).sign) {
                1 -> true
                -1 -> false
                0 -> false //return@count false
                else -> error("Unexpected sign result for ${numbers[0]} - ${numbers[1]}")
            }

            val firstUnsafeIndex = (1..<numbers.size).firstOrNull { i ->
                !isPairSafe(numbers, i, increasing)
            }
            println("First unsafe: $firstUnsafeIndex (${firstUnsafeIndex?.let { numbers[it]}})")

            if (firstUnsafeIndex == null) return@count true.also { println("SAFE without removing")}

            val toleratedNumbers =
                numbers.subList(0, firstUnsafeIndex) + numbers.subList(firstUnsafeIndex+1, numbers.size)
            println("Tolerated numbers: $toleratedNumbers")

            (1..<toleratedNumbers.size).all { i ->
                isPairSafe(toleratedNumbers, i, increasing)
            }.also { println("Post-removal result? $it")}
        }

    println("Day $DAY")
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println("Part1 output: $it") } == PART1_CHECK)
    check(part2(testInput).also { println("Part2 output: $it") } == PART2_CHECK)

    val input = readInput("Day$DAY")
    println("Part1 final output: ${part1(input)}")
    println("Part2 final output: ${part2(input)}")
}
