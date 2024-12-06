@file:Suppress("MagicNumber", "ReplacePrintlnWithLogging")

private const val DAY = "06"

private const val PART1_CHECK = 41
private const val PART2_CHECK = 6

private enum class Orientation(
    val dy: Int,
    val dx: Int,
) {
    UP(-1, 0),
    RIGHT(0, 1),
    DOWN(1, 0),
    LEFT(0, -1),
}

private fun Orientation.rotate() =
    when (this) {
        Orientation.UP -> Orientation.RIGHT
        Orientation.RIGHT -> Orientation.DOWN
        Orientation.DOWN -> Orientation.LEFT
        Orientation.LEFT -> Orientation.UP
    }

private fun move(
    x: Int,
    y: Int,
    orientation: Orientation,
): Pair<Int, Int> {
    val nextY = y + orientation.dy
    val nextX = x + orientation.dx
    return Pair(nextX, nextY)
}

fun main() {
    fun part1(input: List<String>): Int {
        val height = input.size
        val width = input.first().length
        val visited =
            List(height) {
                MutableList(width) { false }
            }
        var guardX: Int = -1
        var guardY: Int = -1

        initGuard@ for (y in 0..<height) {
            for (x in 0..<width) {
                if (input[y].get(x) == '^') {
                    guardY = y
                    guardX = x
                    break@initGuard
                }
            }
        }
        var guardOrientation = Orientation.UP
        while (true) {
            visited[guardY][guardX] = true
            val (considerX, considerY) = move(guardX, guardY, guardOrientation)
            if (considerY !in 0..<height || considerX !in 0..<width) {
                break
            }
            if (input[considerY][considerX] == '#') {
                guardOrientation = guardOrientation.rotate()
                var (newGuardX, newGuardY) = move(guardX, guardY, guardOrientation)
                guardX = newGuardX
                guardY = newGuardY
            } else {
                guardX = considerX
                guardY = considerY
            }
        }
        return visited.sumOf { line ->
            line.count { it }
        }
    }

    fun part2(input: List<String>): Int {
        val height = input.size
        val width = input.first().length
        var initGuardX: Int = -1
        var initGuardY: Int = -1

        initGuard@ for (y in 0..<height) {
            for (x in 0..<width) {
                if (input[y].get(x) == '^') {
                    initGuardY = y
                    initGuardX = x
                    break@initGuard
                }
            }
        }

        fun isLoop(
            obstacleX: Int,
            obstacleY: Int,
        ): Boolean {
            println("\nEvaluating ($obstacleX, $obstacleY)")
            if (input[obstacleY][obstacleX] == '#') {
                println("Not a loop - an obstacle")
                return false
            }
            val visitedOrientations =
                List(height) {
                    List(width) {
                        mutableSetOf<Orientation>()
                    }
                }
            var guardX = initGuardX
            var guardY = initGuardY
            var guardOrientation = Orientation.UP
            while (true) {
//                println("Guard is at ($guardX, $guardY) facing $guardOrientation")
                if (guardOrientation in visitedOrientations[guardY][guardX]) {
                    println("Guard visited ($guardX, $guardY) before - loop")
                    return true
                }
                visitedOrientations[guardY][guardX].add(guardOrientation)

                val (considerX, considerY) = move(guardX, guardY, guardOrientation)
                if (considerY !in 0..<height || considerX !in 0..<width) {
                    println("Guard left area - not a loop")
                    return false
                }
                if (
                    (considerY == obstacleY && considerX == obstacleX) || input[considerY][considerX] == '#'
                ) {
                    guardOrientation = guardOrientation.rotate()
                    var (newGuardX, newGuardY) = move(guardX, guardY, guardOrientation)
                    guardX = newGuardX
                    guardY = newGuardY
//                    println("Rotated to $guardOrientation")
                } else {
                    guardX = considerX
                    guardY = considerY
                }
            }
        }

        var loops = 0
        for (y in 0..<height) {
            for (x in 0..<width) {
                if (isLoop(y, x)) {
                    loops++
                }
            }
        }
        println("Loop count: $loops")

        return loops
    }

    println("Day $DAY")
    val testInput = readInput("Day${DAY}_test")
//    check(part1(testInput).also { println("Part1 output: $it") } == PART1_CHECK)
    check(part2(testInput).also { println("Part2 output: $it") } == PART2_CHECK)

    val input = readInput("Day$DAY")
//    println("Part1 final output: ${part1(input)}")
    println("Part2 final output: ${part2(input)}")
}
