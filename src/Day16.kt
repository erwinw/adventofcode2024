@file:Suppress("MagicNumber", "ReplacePrintlnWithLogging", "DuplicatedCode")

import Day16Orientation.EAST
import Day16Orientation.NORTH
import Day16Orientation.SOUTH
import Day16Orientation.WEST

private const val DAY = "16"

private const val PART1_CHECK = 11048
private const val PART2_CHECK = 64

enum class Day16Orientation {
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
        operator fun plus(other: Vec2) = Vec2(x + other.x, y + other.y)
    }

    fun Day16Orientation.forward() =
        when (this) {
            NORTH -> Vec2(0, -1)
            EAST -> Vec2(1, 0)
            SOUTH -> Vec2(0, 1)
            WEST -> Vec2(-1, 0)
        }

    fun Day16Orientation.clockwise() =
        when (this) {
            NORTH -> EAST
            EAST -> SOUTH
            SOUTH -> WEST
            WEST -> NORTH
        }

    fun Day16Orientation.counterClockwise() =
        when (this) {
            NORTH -> WEST
            EAST -> NORTH
            SOUTH -> EAST
            WEST -> SOUTH
        }

    data class Position(
        val pos: Vec2,
        val orientation: Day16Orientation,
    )

    data class Progress(
        val pos: Vec2,
        val orientation: Day16Orientation,
        val score: Int,
    )

    fun part1(input: List<String>): Int {
        val map = input.map { it.toList() }
        val height = map.size
        val width = map.first().size
        var start: Vec2 = Vec2(-1, -1)
        var end: Vec2 = Vec2(-1, -1)
        map.forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                if (char == 'S') {
                    start = Vec2(x, y)
                } else if (char == 'E') {
                    end = Vec2(x, y)
                }
            }
        }

        val startProgress = Progress(start, EAST, 0)
        val toProgress = mutableListOf(startProgress to listOf(startProgress))
        val checked = mutableMapOf<Position, List<Progress>>(Position(start, EAST) to listOf(startProgress))

        repeat(10_000_000) { iter ->
            val (head, headPath) = toProgress.first()
            toProgress.removeAt(0)
//            println("$iter: CHECKING $head; remaining: $toProgress")

            // Move forward?
            head.orientation.forward().let { moved ->
                val nextLoc = head.pos + moved
                val position = Position(nextLoc, head.orientation)
                @Suppress("ComplexCondition")
                if (
                    position in checked || nextLoc.x < 0 || nextLoc.y < 0 || nextLoc.x >= width || nextLoc.y >= height
                ) {
                    return@let
                }
                val nextScore = head.score + 1
                val nextChar = map[nextLoc.y][nextLoc.x]
                val nextProgress = Progress(nextLoc, head.orientation, nextScore)
                checked[position] = headPath + nextProgress
                when (nextChar) {
                    'E' -> {
                        println("WINNING PATH: ${headPath + nextProgress}")
                        return nextScore
                    }
                    '.' -> toProgress.add(nextProgress to headPath + nextProgress)
                }
            }

            listOf(
                head.orientation.clockwise(),
                head.orientation.counterClockwise(),
            ).forEach { nextOrientation ->
                val nextLoc = head.pos
                val position = Position(nextLoc, nextOrientation)
                @Suppress("ComplexCondition")
                if (position in checked) {
                    return@forEach
                }
                val nextScore = head.score + 1000
                val nextProgress = Progress(head.pos, nextOrientation, nextScore)
                checked[position] = headPath + nextProgress
                toProgress.add(nextProgress to headPath + nextProgress)
            }

            toProgress.sortBy { it.first.score }
        }
        error("No solution found")
    }

    /**
     * Part 2 is incorrect; it's ignoring branch-and-joins
     */
    fun part2(input: List<String>): Int {
        val map = input.map { it.toList() }
        val height = map.size
        val width = map.first().size
        var start: Vec2 = Vec2(-1, -1)
        var end: Vec2 = Vec2(-1, -1)
        map.forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                if (char == 'S') {
                    start = Vec2(x, y)
                } else if (char == 'E') {
                    end = Vec2(x, y)
                }
            }
        }

        val startProgress = Progress(start, EAST, 0)
        val toProgress = mutableListOf(startProgress to listOf(startProgress))
        val checked = mutableMapOf<Position, List<Progress>>(Position(start, EAST) to listOf(startProgress))

        repeat(10_000_000) { iter ->
            val (head, headPath) = toProgress.first()
            toProgress.removeAt(0)
//            println("$iter: CHECKING $head; remaining: $toProgress")

            // Move forward?
            head.orientation.forward().let { moved ->
                val nextLoc = head.pos + moved
                val position = Position(nextLoc, head.orientation)
                @Suppress("ComplexCondition")
                if (
                    position in checked || nextLoc.x < 0 || nextLoc.y < 0 || nextLoc.x >= width || nextLoc.y >= height
                ) {
                    return@let
                }
                val nextScore = head.score + 1
                val nextChar = map[nextLoc.y][nextLoc.x]
                val nextProgress = Progress(nextLoc, head.orientation, nextScore)
                checked[position] = headPath + nextProgress
                when (nextChar) {
                    'E' -> {
                        val path = (headPath + nextProgress).map { it.pos }.toSet()
                        path.forEach { println("> $it") }
                        return path.size
                    }

                    '.' -> toProgress.add(nextProgress to headPath + nextProgress)
                }
            }

            listOf(
                head.orientation.clockwise(),
                head.orientation.counterClockwise(),
            ).forEach { nextOrientation ->
                val nextLoc = head.pos
                val position = Position(nextLoc, nextOrientation)
                @Suppress("ComplexCondition")
                if (position in checked) {
                    return@forEach
                }
                val nextScore = head.score + 1000
                val nextProgress = Progress(head.pos, nextOrientation, nextScore)
                checked[position] = headPath + nextProgress
                toProgress.add(nextProgress to headPath + nextProgress)
            }

            toProgress.sortBy { it.first.score }
        }
        error("No solution found")
    }

    println("Day $DAY")
    val testInput = readInput("Day${DAY}_test")
//    check(part1(testInput).also { println("Part1 output: $it") } == PART1_CHECK)
    check(part2(testInput).also { println("Part2 output: $it") } == PART2_CHECK)

    val input = readInput("Day$DAY")
//    println("Part1 final output: ${part1(input)}")
    // WRONG: 581 - ?
    // WRONG: 724 - also counted orientation
    println("Part2 final output: ${part2(input)}")
}
