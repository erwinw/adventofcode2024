@file:Suppress("MagicNumber", "ReplacePrintlnWithLogging")

private const val DAY = "05"

private const val PART1_CHECK = 143
private const val PART2_CHECK = 123

fun main() {
    fun isUpdateCorrect(
        update: List<Int>,
        rules: List<List<Int>>,
    ): Boolean =
        rules.all { (page1, page2) ->
            val page1Index = update.indexOf(page1)
            val page2Index = update.indexOf(page2)

            page1Index == -1 || page2Index == -1 || page1Index < page2Index
        }

    fun parseInputToRulesUpdates(input: List<String>): Pair<List<List<Int>>, List<List<Int>>> {
        val rules: MutableList<List<Int>> = mutableListOf()
        val updates: MutableList<List<Int>> = mutableListOf()

        var parsingRules = true
        input.forEach { line ->
            if (line.isEmpty()) {
                parsingRules = false
                return@forEach
            }

            if (parsingRules) {
                rules.add(line.split('|').map { it.toInt() })
            } else {
                updates.add(line.split(',').map { it.toInt() })
            }
        }

        return rules to updates
    }

    fun addSorted(
        sorted: List<Int>,
        p: Int,
        rules: List<List<Int>>,
    ): List<Int> {
        var candidate = listOf(p) + sorted
        if (isUpdateCorrect(candidate, rules)) {
            return candidate
        }
        for (i in 0..<sorted.size) {
            var candidate = sorted.slice(0..i) + listOf(p) + sorted.slice((i + 1)..(sorted.size - 1))
            if (isUpdateCorrect(candidate, rules)) {
                return candidate
            }
        }
        error("FAILED TO FIND CANDIDATE: $p in $sorted")
    }

    fun sortUpdate(
        update: List<Int>,
        rules: List<List<Int>>,
    ): List<Int> =
        update
            .drop(1)
            .fold(listOf(update.first())) { sorted, p ->
                addSorted(sorted, p, rules)
            }

    fun part1(input: List<String>): Int {
        val (rules, updates) = parseInputToRulesUpdates(input)

        return updates
            .filter { update -> isUpdateCorrect(update, rules) }
            .sumOf { update -> update[(update.size - 1).div(2)] }
    }

    fun part2(input: List<String>): Int {
        val (rules, updates) = parseInputToRulesUpdates(input)
        val invalidUpdates = updates.filter { !isUpdateCorrect(it, rules) }
        val sortedUpdates: List<List<Int>> =
            invalidUpdates.map { update ->
                sortUpdate(update, rules)
            }
        return sortedUpdates.sumOf { update ->
            update[(update.size - 1).div(2)]
        }
    }

    println("Day $DAY")
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println("Part1 output: $it") } == PART1_CHECK)
    check(part2(testInput).also { println("Part2 output: $it") } == PART2_CHECK)

    val input = readInput("Day$DAY")
    println("Part1 final output: ${part1(input)}")
    println("Part2 final output: ${part2(input)}")
}
