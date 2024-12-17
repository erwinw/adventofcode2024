@file:Suppress(
    "MagicNumber",
    "ReplacePrintlnWithLogging",
    "DuplicatedCode",
    "DestructuringDeclarationWithTooManyEntries",
)

import kotlin.math.pow

private const val DAY = "17"

private const val PART1_CHECK = "4,6,3,5,6,3,5,2,1,0"
private const val PART2_CHECK = 117440L

enum class Day17Instr(
    val opcode: Int,
) {
    ADV(0),
    BXL(1),
    BST(2),
    JNZ(3),
    BXC(4),
    OUT(5),
    BDV(6),
    CDV(7),
}

fun main() {
    fun execute(
        program: List<Int>,
        inRegA: Long,
        inRegB: Long = 0,
        inRegC: Long = 0,
    ): List<Int> {
        var regA = inRegA
        var regB = inRegB
        var regC = inRegC

        var instructionPointer = 0
        var output = mutableListOf<Int>()

        fun combo(operand: Int): Long =
            when (operand) {
                0, 1, 2, 3 -> operand.toLong()
                4 -> regA
                5 -> regB
                6 -> regC
                else -> error("Invalid combo operand '$operand'")
            }

        while (instructionPointer < program.size) {
            val opcodeInt = program[instructionPointer]
            val opcode = Day17Instr.entries.first { it.opcode == opcodeInt }
            val operand = program[instructionPointer + 1]

            when (opcode) {
                Day17Instr.ADV -> {
                    val denominator = (2.toDouble().pow(combo(operand).toDouble())).toLong()
                    regA /= denominator
                }

                Day17Instr.BXL -> {
                    regB = regB xor operand.toLong()
                }

                Day17Instr.BST -> {
                    regB = combo(operand) % 8
                }

                Day17Instr.JNZ -> {
                    if (regA != 0L) {
                        instructionPointer = operand
                        continue
                    }
                }

                Day17Instr.BXC -> {
                    regB = regB xor regC
                }

                Day17Instr.OUT -> {
                    output += (combo(operand) % 8).toInt()
                }

                Day17Instr.BDV -> {
                    val denominator = (2.toDouble().pow(combo(operand).toDouble())).toLong()
                    regB = regA / denominator
                }

                Day17Instr.CDV -> {
                    val denominator = (2.toDouble().pow(combo(operand).toDouble())).toLong()
                    regC = regA / denominator
                }
            }

            instructionPointer += 2
        }

        return output // .joinToString(",")
    }

    fun part1(input: List<String>): String {
        var regA: Long = input[0].substringAfter("Register A: ").toLong()
        var regB: Long = input[1].substringAfter("Register B: ").toLong()
        var regC: Long = input[2].substringAfter("Register C: ").toLong()

        var program: List<Int> =
            input[4]
                .substringAfter("Program: ")
                .split(',')
                .map(String::toInt)

        return execute(program, regA, regB, regC).joinToString(",")
    }

    fun findQuine(
        program: List<Int>,
        target: List<Int>,
    ): Long {
        var aStart =
            if (target.size == 1) {
                0
            } else {
                8 * findQuine(program, target.drop(1))
            }

        while (execute(program, aStart) != target) {
            aStart++
        }

        return aStart
    }

    fun part2(input: List<String>): Long {
        val rawProgram = input[4].substringAfter("Program: ")
        var program: List<Int> =
            rawProgram
                .split(',')
                .map(String::toInt)

        return findQuine(program, program)
    }

    println("Day $DAY")
    val testAInput = readInput("Day${DAY}_testA")
    check(part1(testAInput).also { println("Part1 output: $it") } == PART1_CHECK)
    val testBInput = readInput("Day${DAY}_testB")
    check(part2(testBInput).also { println("Part2 output: $it") } == PART2_CHECK)

    val input = readInput("Day$DAY")
    println("Part1 final output: ${part1(input)}")
    // 265061364597659
    println("Part2 final output: ${part2(input)}")
}
