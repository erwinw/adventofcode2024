@file:Suppress("MagicNumber", "ReplacePrintlnWithLogging")

private const val DAY = "03"

private const val PART1_CHECK = 161
private const val PART2_CHECK = 48

fun main() {
    val regexPart01 = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")
    val regexPart02 = Regex("""mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\)""")

    fun part1(input: List<String>): Int =
        input.sumOf { line ->
            regexPart01
                .findAll(line)
                .sumOf { match ->
                    val (a, b) = match.destructured
                    a.toInt() * b.toInt()
                }
        }

    fun part2(input: List<String>): Int {
        var enabled = true
        return input.sumOf { line ->
            regexPart02
                .findAll(line)
                .sumOf { match ->
                    when (match.value) {
                        "do()" -> {
                            enabled = true
                            0
                        }

                        "don\'t()" -> {
                            enabled = false
                            0
                        }

                        else -> {
                            if (enabled) {
                                val (a, b) = match.destructured
                                a.toInt() * b.toInt()
                            } else {
                                0
                            }
                        }
                    }
                }
        }
    }

    println("Day $DAY")
    val testInput1 = readInput("Day${DAY}_test1")
    val testInput2 = readInput("Day${DAY}_test2")
    check(part1(testInput1).also { println("\nPart1 output: $it") } == PART1_CHECK)
    check(part2(testInput2).also { println("\nPart2 output: $it") } == PART2_CHECK)

    val input = readInput("Day$DAY")
    println("\nPart1 final output: ${part1(input)}")
    println("\nPart2 final output: ${part2(input)}")
}
