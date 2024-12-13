@file:Suppress("MagicNumber", "ReplacePrintlnWithLogging", "DuplicatedCode")

import kotlin.math.abs

private const val DAY = "12"

private const val PART1_CHECK = 1930
private const val PART2_CHECK = 0

fun main() {
    data class Coord(
        val x: Int,
        val y: Int,
    ) {
        fun north() = Coord(x, y - 1)

        fun east() = Coord(x + 1, y)

        fun south() = Coord(x, y + 1)

        fun west() = Coord(x - 1, y)
    }

    data class Area(
        val plant: String,
        val coords: MutableList<Coord>,
    ) {
        constructor(plant: String, coord: Coord) : this(plant, mutableListOf(coord))

        operator fun contains(coord: Coord) = coords.any { it.x == coord.x && it.y == coord.y }
    }

    fun coordsAreNeighbour(
        coordA: Coord,
        coordB: Coord,
    ): Boolean =
        when {
            coordA.x == coordB.x && abs(coordA.y - coordB.y) == 1 -> true
            coordA.y == coordB.y && abs(coordA.x - coordB.x) == 1 -> true
            else -> false
        }

    fun part1(input: List<String>): Int {
        val map = input.map { it.chunked(1) }
        val areas = mutableListOf<Area>()

        map.forEachIndexed { y, row ->
            row.forEachIndexed { x, plant ->
                val coord = Coord(y, x)
                val neighbouringIndexAreas =
                    areas
                        .mapIndexed { index, area -> index to area }
                        .filter { (_, area) ->
                            area.plant == plant && area.coords.any { coordsAreNeighbour(it, coord) }
                        }

                when (neighbouringIndexAreas.size) {
                    0 -> areas.add(Area(plant, coord))
                    1 ->
                        neighbouringIndexAreas
                            .first()
                            .second.coords
                            .add(coord)
                    else -> {
                        val mergingIndexes = neighbouringIndexAreas.map { it.first }.sortedDescending()
                        mergingIndexes.forEach { indexToRemove -> areas.removeAt(indexToRemove) }
                        val mergedArea = Area(plant, coord)
                        mergedArea.coords.addAll(neighbouringIndexAreas.flatMap { it.second.coords })
                        areas.add(mergedArea)
                    }
                }
            }
        }

        fun Area.area(): Int = this.coords.size

        fun Area.perimeter(): Int =
            coords.sumOf { coord ->
                var coordPerimeters = 0
                if (coord.north() !in this) {
                    coordPerimeters++
                }
                if (coord.east() !in this) {
                    coordPerimeters++
                }
                if (coord.south() !in this) {
                    coordPerimeters++
                }
                if (coord.west() !in this) {
                    coordPerimeters++
                }
                coordPerimeters
            }

//        areas.forEach { area ->
//            println("Area '${area.plant}'; area ${area.area()}, perimeter ${area.perimeter()}")
//        }
        //
        return areas.sumOf { area ->
            area.area() * area.perimeter()
        }
    }

    fun part2(input: List<String>): Int = input.size

    println("Day $DAY")
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println("Part1 output: $it") } == PART1_CHECK)
//    check(part2(testInput).also { println("Part2 output: $it") } == PART2_CHECK)

    val input = readInput("Day$DAY")
    println("Part1 final output: ${part1(input)}")
//    println("Part2 final output: ${part2(input)}")
}
