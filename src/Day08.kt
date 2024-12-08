@file:Suppress("MagicNumber", "ReplacePrintlnWithLogging")

import com.sun.tools.javac.jvm.Code.width

private const val DAY = "08"

private const val PART1_CHECK = 14
private const val PART2_CHECK = 34

fun main() {
    fun computeAntennaTypes(input: List<String>): List<String> =
        input
            .flatMap {
                it.toList().map(Char::toString)
            }.toSet()
            .filterNot { it == "." }

    fun mapTypeAntennas(
        antennaTypes: List<String>,
        height: Int,
        width: Int,
        map: List<List<String>>,
    ): Map<String, MutableList<Pair<Int, Int>>> =
        antennaTypes.associate { antennaType ->
            val typeAntennas: MutableList<Pair<Int, Int>> = mutableListOf()

            for (y in 0..<height) {
                for (x in 0..<width) {
                    if (map[y][x] == antennaType) {
                        typeAntennas.add(Pair(x, y))
                    }
                }
            }

            Pair(antennaType, typeAntennas)
        }

    fun part1(input: List<String>): Int {
        val map = input.map { it.chunked(1) }
        val height = map.size
        val width = map.first().size

        val antennaTypes = computeAntennaTypes(input)

        val typeAntennas = mapTypeAntennas(antennaTypes, height, width, map)

        val antiNodesMap = List(height) { MutableList(width) { false } }
        typeAntennas.forEach { (_, antennas) ->
            antennas.forEachIndexed { indexA, (antennaAx, antennaAy) ->
                antennas.forEachIndexed { indexB, (antennaBx, antennaBy) ->
                    if (indexA != indexB) {
                        val diffX = antennaAx - antennaBx
                        val diffY = antennaAy - antennaBy

                        val antiNodeOneX = antennaBx - diffX
                        val antiNodeOneY = antennaBy - diffY
                        val antiNodeTwoX = antennaAx + diffX
                        val antiNodeTwoY = antennaAy + diffY

                        if (antiNodeOneX in (0..<width) && antiNodeOneY in (0..<height)) {
                            antiNodesMap[antiNodeOneY][antiNodeOneX] = true
                        }

                        if (antiNodeTwoX in (0..<width) && antiNodeTwoY in (0..<height)) {
                            antiNodesMap[antiNodeTwoY][antiNodeTwoX] = true
                        }
                    }
                }
            }
        }
        return antiNodesMap.sumOf { row -> row.count { it } }
    }

    fun part2(input: List<String>): Int {
        val map = input.map { it.chunked(1) }
        val height = map.size
        val width = map.first().size

        val antennaTypes = computeAntennaTypes(input)

        val typeAntennas: Map<String, List<Pair<Int, Int>>> =
            mapTypeAntennas(antennaTypes, height, width, map)

        val antiNodesMap = List(height) { MutableList(width) { false } }
        typeAntennas.forEach { (_, antennas) ->
            antennas.forEachIndexed { indexA, (antennaAx, antennaAy) ->
                antennas.forEachIndexed { indexB, (antennaBx, antennaBy) ->
                    if (indexA != indexB) {
                        val diffX = antennaAx - antennaBx
                        val diffY = antennaAy - antennaBy

                        var antiNodeX: Int = antennaAx
                        var antiNodeY: Int = antennaAy
                        do {
                            antiNodesMap[antiNodeY][antiNodeX] = true
                            antiNodeX += diffX
                            antiNodeY += diffY
                        } while (antiNodeX in (0..<width) && antiNodeY in (0..<height))

                        antiNodeX = antennaAx
                        antiNodeY = antennaAy
                        do {
                            antiNodesMap[antiNodeY][antiNodeX] = true
                            antiNodeX -= diffX
                            antiNodeY -= diffY
                        } while (antiNodeX in (0..<width) && antiNodeY in (0..<height))
                    }
                }
            }
        }

        return antiNodesMap.sumOf { row -> row.count { it } }
    }

    println("Day $DAY")
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println("Part1 output: $it") } == PART1_CHECK)
    check(part2(testInput).also { println("Part2 output: $it") } == PART2_CHECK)

    val input = readInput("Day$DAY")
    println("Part1 final output: ${part1(input)}")
    println("Part2 final output: ${part2(input)}")
}
