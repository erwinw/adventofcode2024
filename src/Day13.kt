@file:Suppress("MagicNumber", "ReplacePrintlnWithLogging", "DuplicatedCode")

private const val DAY = "13"

private const val PART1_CHECK = 480L

fun main() {
    data class Vec2(
        val x: Long,
        val y: Long,
    ) {
        operator fun plus(other: Vec2): Vec2 = Vec2(x + other.x, y + other.y)

        operator fun times(multiplier: Int): Vec2 = Vec2(x * multiplier, y * multiplier)
    }

    data class Game(
        val buttonA: Vec2,
        val buttonB: Vec2,
        val prize: Vec2,
    )

    // Thanks Linear Algebra - Cramer's Rule
    fun minScoreGame(game: Game): Long {
        val determinant = game.buttonA.x * game.buttonB.y - game.buttonA.y * game.buttonB.x
        val a = (game.prize.x * game.buttonB.y - game.prize.y * game.buttonB.x) / determinant
        val b = (game.buttonA.x * game.prize.y - game.buttonA.y * game.prize.x) / determinant
        return if (
            game.buttonA.x * a + game.buttonB.x * b == game.prize.x &&
            game.buttonA.y * a + game.buttonB.y * b == game.prize.y
        ) {
            a * 3 + b
        } else {
            0
        }
    }

    fun part1(input: List<String>): Long {
        val games =
            input.chunked(4).map { gameLines ->
                val (aX, aY) = Regex("Button A: X\\+(\\d+), Y\\+(\\d+)").find(gameLines[0])!!.destructured
                val (bX, bY) = Regex("Button B: X\\+(\\d+), Y\\+(\\d+)").find(gameLines[1])!!.destructured
                val (prizeX, prizeY) = Regex("Prize: X=(\\d+), Y=(\\d+)").find(gameLines[2])!!.destructured
                Game(
                    Vec2(aX.toLong(), aY.toLong()),
                    Vec2(bX.toLong(), bY.toLong()),
                    Vec2(prizeX.toLong(), prizeY.toLong()),
                )
            }

        return games.sumOf(::minScoreGame)

        /*
        return games.sumOf { game ->
            var minTokens = Int.MAX_VALUE

            for (aPresses in 1..100) {
                for (bPresses in 1..100) {
                    val location = (game.buttonA * aPresses) + (game.buttonB * bPresses)
                    val tokens = aPresses * 3 + bPresses * 1
                    if (location == game.prize && tokens < minTokens) {
                        minTokens = tokens
                    }
                }
            }

            minTokens.takeUnless { it == Int.MAX_VALUE } ?: 0
        }
         */
    }

    /*
    ax * aPress + bx * bPress ==
     */
    fun part2(input: List<String>): Long {
        val scoreAdder = 10000000000000
        val games =
            input.chunked(4).map { gameLines ->
                val (aX, aY) = Regex("Button A: X\\+(\\d+), Y\\+(\\d+)").find(gameLines[0])!!.destructured
                val (bX, bY) = Regex("Button B: X\\+(\\d+), Y\\+(\\d+)").find(gameLines[1])!!.destructured
                val (prizeX, prizeY) = Regex("Prize: X=(\\d+), Y=(\\d+)").find(gameLines[2])!!.destructured
                Game(
                    Vec2(aX.toLong(), aY.toLong()),
                    Vec2(bX.toLong(), bY.toLong()),
                    Vec2(prizeX.toLong() + scoreAdder, prizeY.toLong() + scoreAdder),
                )
            }

        return games.sumOf(::minScoreGame)
    }

    println("Day $DAY")
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println("Part1 output: $it") } == PART1_CHECK)

    val input = readInput("Day$DAY")
    // 27157
    println("Part1 final output: ${part1(input)}")
    // 104015411578548
    println("Part2 final output: ${part2(input)}")
}
