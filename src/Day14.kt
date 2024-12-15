@file:Suppress("MagicNumber", "ReplacePrintlnWithLogging", "DuplicatedCode")

private const val DAY = "14"

private const val PART1_CHECK = 12

fun main() {
    data class Vec2(
        var x: Long,
        var y: Long,
    ) {
        operator fun plus(other: Vec2): Vec2 = Vec2(x + other.x, y + other.y)

        operator fun times(multiplier: Int): Vec2 = Vec2(x * multiplier, y * multiplier)

        fun normalize(
            width: Long,
            height: Long,
        ): Vec2 =
            apply {
                while (x < 0) {
                    x += width
                }
                while (y < 0) {
                    y += height
                }
            }
    }

    data class Robot(
        val p: Vec2,
        val v: Vec2,
        val width: Long,
        val height: Long,
    ) {
        fun pos(round: Int): Vec2 {
            val total = p + v * round
            return Vec2(
                total.x % width,
                total.y % height,
            ).normalize(width, height)
        }
    }

    fun printMap(
        robotsPos: List<Vec2>,
        width: Long,
        height: Long,
    ) {
        val map = List(height.toInt()) { MutableList(width.toInt()) { 0 } }
        robotsPos.forEach { (x, y) ->
            map[y.toInt()][x.toInt()] += 1
        }
        println("MAP:")
        map.forEachIndexed { y, row ->
            println(
                row
                    .mapIndexed { x, cell -> x to cell }
                    .joinToString("") { (x, cell) ->
                        when {
                            cell == 0 -> "."
                            else -> cell.toString()
                        }
                    },
            )
        }
        println("")
    }

    @Suppress("DestructuringDeclarationWithTooManyEntries")
    fun part1(
        input: List<String>,
        width: Long,
        height: Long,
    ): Int {
        val halfWidth = ((width - 1) / 2).toLong()
        val halfHeight = ((height - 1) / 2).toLong()
        val robots =
            input.map { line ->
                val (pX, pY, vX, vY) = Regex("""p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""").find(line)!!.destructured
                val p = Vec2(pX.toLong(), pY.toLong())
                val v = Vec2(vX.toLong(), vY.toLong())
                Robot(p = p, v = v, width = width, height = height)
            }
        val robotsPos = robots.map { it.pos(100) }
        printMap(robotsPos, width, height)

        val quadsCount = mutableListOf(0, 0, 0, 0)
        robotsPos.forEach { (x, y) ->
            if (x == halfWidth || y == halfHeight) {
                return@forEach
            }
            when {
                x < halfWidth && y < halfHeight -> quadsCount[0]++
                x > halfWidth && y < halfHeight -> quadsCount[1]++
                x > halfWidth -> quadsCount[2]++
                else -> quadsCount[3]++
            }
        }
        println("Quads: $quadsCount")
        return quadsCount.reduce { a, b -> a * b }
    }

    @Suppress("DestructuringDeclarationWithTooManyEntries")
    fun part2(
        input: List<String>,
        width: Long,
        height: Long,
    ): Int {
        val robots =
            input.map { line ->
                val (pX, pY, vX, vY) = Regex("""p=(-?\d+),(-?\d+) v=(-?\d+),(-?\d+)""").find(line)!!.destructured
                val p = Vec2(pX.toLong(), pY.toLong())
                val v = Vec2(vX.toLong(), vY.toLong())
                Robot(p = p, v = v, width = width, height = height)
            }
        for (iter in 1..Int.MAX_VALUE) {
            val robotsPos = robots.map { it.pos(iter) }
            if (robotsPos.distinct().size == robots.size) {
                printMap(robotsPos, width, height)
                return iter
            }
        }
        error("Giving up")
    }

    println("Day $DAY")
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput, width = 11, height = 7).also { println("Part1 output: $it") } == PART1_CHECK)

    val input = readInput("Day$DAY")
    println("Part1 final output: ${part1(input, width = 101, height = 103)}")
    println("Part2 final output: ${part2(input, width = 101, height = 103)}")
}
