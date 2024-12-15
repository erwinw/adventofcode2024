@file:Suppress("MagicNumber", "ReplacePrintlnWithLogging", "DuplicatedCode")

private const val DAY = "15"

private const val PART1A_CHECK = 2028
private const val PART1B_CHECK = 10092
private const val PART2_CHECK = 9021

fun main() {
    data class Vec2(
        var x: Int,
        var y: Int,
    ) {
        operator fun plus(other: Vec2): Vec2 = Vec2(x + other.x, y + other.y)
    }

    fun itemsToMove1(
        startPosition: Vec2,
        direction: Vec2,
        map: List<MutableList<String>>,
    ): List<Vec2>? {
        var position = startPosition.copy()
        var obstacles = mutableListOf<Vec2>(position)
        do {
            position += direction
            if (map[position.y][position.x] == ".") {
                return obstacles
            }
            obstacles.add(position)
        } while (map[position.y][position.x] != "#")
        return null
    }

    fun printMap1(map: List<MutableList<String>>) {
        map.forEach { println(it.joinToString("")) }
    }

    fun part1(input: List<String>): Int {
        val mapLines = input.takeWhile { it.isNotEmpty() }
        val moveLines = input.takeLastWhile { it.isNotEmpty() }

        var map: List<MutableList<String>> = mapLines.map { it.chunked(1).toMutableList() }
        val moves = moveLines.joinToString("")
        var robot = Vec2(-1, -1)
        map.forEachIndexed { y, row ->
            row.forEachIndexed { x, space ->
                if (space == "@") {
                    robot.y = y
                    robot.x = x
                }
            }
        }
//        println("Initial state:")
//        printMap1(map)

        moves.forEachIndexed { idx, move ->
            val direction =
                when (move) {
                    '^' -> Vec2(0, -1)
                    '>' -> Vec2(1, 0)
                    'v' -> Vec2(0, 1)
                    '<' -> Vec2(-1, 0)
                    else -> error("Unexpected move: '$move'")
                }
            val items = itemsToMove1(robot, direction, map)
            items
                ?.map { it to map[it.y][it.x] }
                ?.forEach { (item, itemValue) ->
                    val updatedItem = item + direction
                    map[updatedItem.y][updatedItem.x] = itemValue
                }
            if (items != null) {
                map[robot.y][robot.x] = "."
                robot += direction
            }
        }

        val boxes: MutableList<Vec2> = mutableListOf()
        map.forEachIndexed { y, row ->
            row.forEachIndexed { x, space ->
                if (space == "O") {
                    boxes += Vec2(x, y)
                }
            }
        }

        return boxes.sumOf { box ->
            100 * box.y + box.x
        }
    }

    fun itemsToMove2(
        startPosition: Vec2,
        direction: Vec2,
        walls: List<Vec2>,
        boxes: List<Vec2>,
    ): Set<Vec2>? {
        val initialPosition = startPosition + direction
        if (initialPosition in walls) {
            return null
        }
        val initialBox = boxes.firstOrNull { it == initialPosition || (it + Vec2(1, 0)) == initialPosition }
        if (initialBox == null) {
            return emptySet()
        }
        val boxesToMove = mutableSetOf(initialBox)
        val boxesToCheck = mutableSetOf(initialBox)
        while (boxesToCheck.isNotEmpty()) {
            val checkingBox = boxesToCheck.first()
            boxesToCheck.remove(checkingBox)

            val movedBox = checkingBox + direction
            val shiftedBox = movedBox + Vec2(1, 0)

            if (movedBox in walls) {
                return null
            }
            if (shiftedBox in walls) {
                return null
            }

            val newBoxes =
                boxes.filter {
                    it !in boxesToMove &&
                        (
                            it == movedBox ||
                                (it + Vec2(1, 0)) == movedBox ||
                                it == shiftedBox ||
                                (it + Vec2(1, 0)) == shiftedBox
                        )
                }
            boxesToMove += newBoxes
            boxesToCheck += newBoxes
        }
        return boxesToMove
    }

    fun printMap2(
        width: Int,
        height: Int,
        robot: Vec2,
        walls: List<Vec2>,
        boxes: List<Vec2>,
    ) {
        val map =
            List(height) {
                MutableList(width) { "." }
            }
        map[robot.y][robot.x] = "@"
        walls.forEach { wall ->
            map[wall.y][wall.x] = "#"
        }
        boxes.forEach { box ->
            map[box.y][box.x] = "["
            map[box.y][box.x + 1] = "]"
        }
        map.forEach { row ->
            println(row.joinToString(""))
        }
    }

    fun part2(input: List<String>): Int {
        val mapLines = input.takeWhile { it.isNotEmpty() }
        val moveLines = input.takeLastWhile { it.isNotEmpty() }

        val moves = moveLines.joinToString("")
        var robot = Vec2(-1, -1)
        val walls = mutableListOf<Vec2>()
        val boxes = mutableListOf<Vec2>()
        val height = mapLines.size
        val width = mapLines.first().length * 2
        mapLines
            .forEachIndexed { y, row ->
                row.forEachIndexed { x, space ->
                    when (space) {
                        '@' -> {
                            robot.y = y
                            robot.x = x * 2
                        }
                        '#' -> {
                            walls.add(Vec2(x * 2, y))
                            walls.add(Vec2(x * 2 + 1, y))
                        }
                        'O' -> {
                            boxes.add(Vec2(x * 2, y))
                        }
                    }
                }
            }

//        printMap2(width, height, robot, walls, boxes)

        moves.forEachIndexed { idx, move ->
//            println("Move $move")

            val direction =
                when (move) {
                    '^' -> Vec2(0, -1)
                    '>' -> Vec2(1, 0)
                    'v' -> Vec2(0, 1)
                    '<' -> Vec2(-1, 0)
                    else -> error("Unexpected move: '$move'")
                }

            val boxesToMove = itemsToMove2(robot, direction, walls, boxes)
//            println("Boxes to move: $boxesToMove")
            if (boxesToMove != null) {
                robot += direction
                boxesToMove.forEach { boxToMove ->
                    when (move) {
                        '^' -> boxToMove.y -= 1
                        '>' -> boxToMove.x += 1
                        'v' -> boxToMove.y += 1
                        '<' -> boxToMove.x -= 1
                    }
                }
//                println("Boxes to move: $boxesToMove")
            }

//            printMap2(width, height, robot, walls, boxes)
//            println("")
        }

        return boxes.sumOf { box ->
            100 * box.y + box.x
        }
    }

    println("Day $DAY")
    val testAInput = readInput("Day${DAY}_testA")
    check(part1(testAInput).also { println("Part1 output: $it") } == PART1A_CHECK)

    val testBInput = readInput("Day${DAY}_testB")
    check(part1(testBInput).also { println("Part1 output: $it") } == PART1B_CHECK)
    check(part2(testBInput).also { println("Part1 output: $it") } == PART2_CHECK)

//    val testCInput = readInput("Day${DAY}_testC")
//    check(part2(testCInput).also { println("Part2 output: $it") } == PART2_CHECK)

    val input = readInput("Day$DAY")
    println("Part1 final output: ${part1(input)}")
    println("Part2 final output: ${part2(input)}")
}
