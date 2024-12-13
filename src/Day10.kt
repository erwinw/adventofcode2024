@file:Suppress("MagicNumber", "ReplacePrintlnWithLogging", "DuplicatedCode")

private const val DAY = "10"

private const val PART1_CHECK = 36
private const val PART2_CHECK = 81

private data class Coord(
    val x: Int,
    val y: Int,
)

fun main() {
    fun part1(input: List<String>): Int {
        val map: List<List<Int>> = input.map { row -> row.chunked(1).map(String::toInt) }
        val height = map.size
        val width = map.first().size
        val trailHeads =
            buildList {
                map.forEachIndexed { y, row ->
                    row.forEachIndexed { x, value ->
                        if (value == 0) {
                            add(Coord(x, y))
                        }
                    }
                }
            }

        fun reachableFrom(start: Coord): Set<Coord> {
            val targetValue = map[start.y][start.x] + 1

            return buildSet {
                if (start.x > 0 && map[start.y][start.x - 1] == targetValue) {
                    add(Coord(start.x - 1, start.y))
                }
                if (start.x < (width - 1) && map[start.y][start.x + 1] == targetValue) {
                    add(Coord(start.x + 1, start.y))
                }
                if (start.y > 0 && map[start.y - 1][start.x ] == targetValue) {
                    add(Coord(start.x, start.y - 1))
                }
                if (start.y < (height - 1) && map[start.y + 1 ][start.x] == targetValue) {
                    add(Coord(start.x, start.y + 1))
                }
            }
        }

        fun reachableFrom(starts: Collection<Coord>): Set<Coord> =
            buildSet {
                starts.map { addAll(reachableFrom(it)) }
            }

        return trailHeads.sumOf { coord ->
            var reachable = setOf(coord)
            repeat(9) { idx ->
//                println("idx $idx reachable: $reachable")
                reachable = reachableFrom(reachable)
            }

            reachable.size
        }
    }

    fun part2(input: List<String>): Int {
        val map: List<List<Int>> = input.map { row -> row.chunked(1).map(String::toInt) }
        val height = map.size
        val width = map.first().size
        val trailHeads =
            buildList {
                map.forEachIndexed { y, row ->
                    row.forEachIndexed { x, value ->
                        if (value == 0) {
                            add(Coord(x, y))
                        }
                    }
                }
            }

        fun continueRoute(route: List<Coord>): List<List<Coord>> {
            val tail = route.last()
            val targetValue = map[tail.y][tail.x] + 1
            return buildList {
                if (tail.x > 0 && map[tail.y][tail.x - 1] == targetValue) {
                    add(route + Coord(tail.x - 1, tail.y))
                }
                if (tail.x < (width - 1) && map[tail.y][tail.x + 1] == targetValue) {
                    add(route + Coord(tail.x + 1, tail.y))
                }
                if (tail.y > 0 && map[tail.y - 1][tail.x ] == targetValue) {
                    add(route + Coord(tail.x, tail.y - 1))
                }
                if (tail.y < (height - 1) && map[tail.y + 1 ][tail.x] == targetValue) {
                    add(route + Coord(tail.x, tail.y + 1))
                }
            }
        }

        fun continueRoutes(routes: Set<List<Coord>>): Set<List<Coord>> = routes.flatMap(::continueRoute).toSet()

        return trailHeads.sumOf { coord ->
            var routes = setOf(listOf(coord))
            repeat(9) { idx ->
                routes = continueRoutes(routes)
            }

            routes.size
        }
    }

    println("Day $DAY")
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println("Part1 output: $it") } == PART1_CHECK)
    check(part2(testInput).also { println("Part2 output: $it") } == PART2_CHECK)

    val input = readInput("Day$DAY")
    println("Part1 final output: ${part1(input)}")
    // 789
    println("Part2 final output: ${part2(input)}")
    // 1735
}
