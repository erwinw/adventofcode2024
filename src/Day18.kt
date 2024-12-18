@file:Suppress("MagicNumber", "ReplacePrintlnWithLogging", "DuplicatedCode")

import kotlin.math.ceil
import kotlin.math.log10

private const val DAY = "18"

private const val PART1_CHECK = 22
private const val PART2_CHECK = "6,1"

enum class Day18Dir {
    NORTH,
    EAST,
    SOUTH,
    WEST,
}

fun main() {
    data class Vec2(
        val x: Int,
        val y: Int,
    ) {
        fun move(
            direction: Day18Dir,
            width: Int,
            height: Int,
        ): Vec2? =
            when (direction) {
                Day18Dir.NORTH -> Vec2(x, y - 1)
                Day18Dir.EAST -> Vec2(x + 1, y)
                Day18Dir.SOUTH -> Vec2(x, y + 1)
                Day18Dir.WEST -> Vec2(x - 1, y)
            }.takeIf { it.x in 0..<width && it.y in 0..<height }
    }

    fun printMap(
        width: Int,
        height: Int,
        wallMap: List<List<Boolean>>,
        distanceMap: List<List<Int>>,
    ) {
        val cellWidth = ceil(log10(width.toDouble() * height.toDouble())).toInt()
        repeat(height) { y ->
            print("| ")
            repeat(width) { x ->
                if (wallMap[y][x]) {
                    print("#".repeat(cellWidth) + " ")
                } else if (distanceMap[y][x] == -1) {
                    print(" ".repeat(cellWidth) + " ")
                } else {
                    print(distanceMap[y][x].toString().padStart(cellWidth) + " ")
                }
            }
            println(" |\n")
        }
    }

    fun getPathDistance(
        width: Int,
        height: Int,
        wallMapItems: List<Vec2>,
    ): Int? {
//        println("ITEMS: ${wallMapItems.size}")
//        wallMapItems.forEachIndexed { idx, item -> println("> $idx: $item") }
        val wallMap = List(height) { MutableList(width) { false } }
        wallMapItems.forEach { (x, y) -> wallMap[y][x] = true }
        val distanceMap =
            List(height) {
                MutableList(width) { -1 }
            }
        distanceMap[0][0] = 0

//        printMap(width, height, wallMap, distanceMap)

        val toCheckItems = mutableListOf(Vec2(0, 0) to 0)
        while (toCheckItems.isNotEmpty()) {
            val (toCheck, toCheckScore) = toCheckItems.first()
            toCheckItems.removeAt(0)
//            println("Checking $toCheck at score $toCheckScore; have ${toCheckItems.size} items remaining")

            Day18Dir.entries.forEach { direction ->
                val newPos =
                    toCheck.move(direction, width, height).also {
//                        println("Moved $direction to $it")
                    } ?: return@forEach
                if (newPos.x + 1 == width && newPos.y + 1 == height) {
                    return toCheckScore + 1
                }
                if (wallMap[newPos.y][newPos.x]) return@forEach
                distanceMap[newPos.y][newPos.x].let { existingScore ->
                    if (existingScore != -1 && toCheckScore + 1 >= existingScore) {
//                        println("Have existing score")
                        return@forEach
                    }
                }

                distanceMap[newPos.y][newPos.x] = toCheckScore + 1
                toCheckItems.add(newPos to (toCheckScore + 1))
//                println("Added $newPos at ${toCheckScore + 1}")
            }

            toCheckItems.sortBy { it.second }
//            println("ABOUT TO LOOP WITH ${toCheckItems.size} items: $toCheckItems")
//            printMap(width, height, wallMap, distanceMap)
        }

        return null
    }

    fun part1(
        input: List<String>,
        limit: Int,
        width: Int,
        height: Int,
    ): Int {
        val wallMapItems =
            input.map {
                val (x, y) = it.split(',').map(String::toInt)
                Vec2(x, y)
            }

        return getPathDistance(width, height, wallMapItems.take(limit)) ?: -1
    }

    fun part2(
        input: List<String>,
        width: Int,
        height: Int,
    ): String {
        val wallMapItems =
            input.map {
                val (x, y) = it.split(',').map(String::toInt)
                Vec2(x, y)
            }
        for (iter in 1..<input.size) {
            val pathDistance = getPathDistance(width, height, wallMapItems.take(iter + 1))
            val winner = wallMapItems[iter]
//            println("Iter $iter, item $winner distance: $pathDistance")
            if (pathDistance == null) {
                return "${winner.x},${winner.y}"
            }
        }
        error("Not found")
    }

    println("Day $DAY")
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput, limit = 12, width = 7, height = 7).also { println("Part1 output: $it") } == PART1_CHECK)
    check(part2(testInput, width = 7, height = 7).also { println("Part2 output: $it") } == PART2_CHECK)

    val input = readInput("Day$DAY")
    println("Part1 final output: ${part1(input, limit = 1024, width = 71, height = 71)}")
    // Result: 24,48
    println("Part2 final output: ${part2(input, width = 71, height = 71)}")
}
