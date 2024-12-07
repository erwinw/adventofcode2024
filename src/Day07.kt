@file:Suppress("MagicNumber", "ReplacePrintlnWithLogging")

private const val DAY = "07"

private const val PART1_CHECK = 3749L
private const val PART2_CHECK = 11387L

enum class Part1Operator {
    PLUS,
    MULTIPLY,
}

enum class Part2Operator {
    PLUS,
    MULTIPLY,
    CONCATENATE,
}

fun main() {
    fun part1(input: List<String>): Long {
        fun tailValues(operands: List<Long>): List<Long> {
            if (operands.size == 1) {
                return operands
            }
            val tail = operands.last()
            val heads = tailValues(operands.dropLast(1))
            return Part1Operator.entries.flatMap { operator ->
                when (operator) {
                    Part1Operator.PLUS ->
                        heads.map {
                            val result = it + tail
                            result
                        }
                    Part1Operator.MULTIPLY ->
                        heads.map {
                            val result = it * tail
                            result
                        }
                }
            }
        }

        fun matches(
            testValue: Long,
            operands: List<Long>,
        ): Boolean =
            tailValues(operands).any {
                it == testValue
            }

        val calibrations: List<Pair<Long, List<Long>>> =
            input.map { line ->
                val (calibration, operandsStr) = line.split(":")
                calibration.toLong() to operandsStr.trim().split(' ').map(String::toLong)
            }
        return calibrations.sumOf { (testValue, operands) ->
            if (matches(testValue, operands)) {
                testValue
            } else {
                0
            }
        }
    }

    fun part2(input: List<String>): Long {
        fun tailValues(operands: List<Long>): List<Long> {
            if (operands.size == 1) {
                return operands
            }
            val tail = operands.last()
            val heads = tailValues(operands.dropLast(1))
            return Part2Operator.entries.flatMap { operator ->
                when (operator) {
                    Part2Operator.PLUS ->
                        heads.map {
                            val result = it + tail
                            result
                        }
                    Part2Operator.MULTIPLY ->
                        heads.map {
                            val result = it * tail
                            result
                        }
                    Part2Operator.CONCATENATE ->
                        heads.map {
                            val result = "$it$tail".toLong()
                            result
                        }
                }
            }
        }

        fun matches(
            testValue: Long,
            operands: List<Long>,
        ): Boolean =
            tailValues(operands).any {
                it == testValue
            }

        val calibrations: List<Pair<Long, List<Long>>> =
            input.map { line ->
                val (calibration, operandsStr) = line.split(":")
                calibration.toLong() to operandsStr.trim().split(' ').map(String::toLong)
            }
        return calibrations.sumOf { (testValue, operands) ->
            if (matches(testValue, operands)) {
                testValue
            } else {
                0
            }
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
