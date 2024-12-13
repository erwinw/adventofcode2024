@file:Suppress("MagicNumber", "ReplacePrintlnWithLogging")

private const val DAY = "09"

private const val PART1_CHECK = 1928L
private const val PART2_CHECK = 2858L

private sealed interface DiskItem {
    val size: Int
}

data class FreeSpace(
    override val size: Int,
) : DiskItem

data class File(
    val fileId: Int,
    override val size: Int,
) : DiskItem

fun main() {
    fun printDisk(disk: List<Int>) {
        println(
            "Disk: ${disk.joinToString("") {
                if (it < 0) {
                    "."
                } else {
                    it.toString()
                }
            }}",
        )
    }

    fun printDisk(disk: List<DiskItem>) {
        println(
            "Disk: ${disk.joinToString("|") { item ->
                when (item) {
                    is FreeSpace -> ".".repeat(item.size)
                    is File -> item.fileId.toString().repeat(item.size)
                }
            }})",
        )
    }

    fun pack1(disk: List<Int>): List<Int> {
        val localDisk = disk.toMutableList()
        val size = localDisk.size
        for (srcBlockIndex in (size - 1) downTo 0) {
//            printDisk(localDisk)
            val blockValue = localDisk[srcBlockIndex]
            if (blockValue >= 0) {
                // contains a file block; let's find a slot to put it in.
                var tgtBlockIndex = -1
                for (tgtBlock in 0..<srcBlockIndex) {
                    if (localDisk[tgtBlock] < 0) {
                        // is empty; we have found our candidate
                        tgtBlockIndex = tgtBlock
                        break
                    }
                }
                if (tgtBlockIndex < 0) {
                    // found no target - cannot optimise any further, so we return
                    return localDisk
                }
                localDisk[tgtBlockIndex] = blockValue
                localDisk[srcBlockIndex] = -1
            }
        }
        return localDisk
    }

    fun part1(input: List<String>): Long {
        val ints = input.first().chunked(1).map(String::toInt)
        val blockCount = ints.sum()
        val disk = MutableList(blockCount) { -1 }

        // build the initial map
        var diskOffset = 0
        var isFile = true
        var fileId = 0
        ints.forEach { item ->
            if (isFile) {
                for (fileOffset in 0..<item) {
                    disk[fileOffset + diskOffset] = fileId
                }

                fileId++
            }
            diskOffset += item

            //
            isFile = !isFile
        }
//        printDisk(disk)

        val result = pack1(disk)
//        printDisk(result)

        return result
            .mapIndexed { index, fileId ->
                if (fileId > 0) {
                    index.toLong() * fileId.toLong()
                } else {
                    0
                }
            }.sum()
    }

    fun part2(input: List<String>): Long {
        var disk = mutableListOf<DiskItem>()

        // build the disk
        var diskOffset = 0
        var isFile = true
        var buildFileId = 0
        input
            .first()
            .chunked(1)
            .map(String::toInt)
            .forEach { item ->
                if (isFile) {
                    disk.add(File(buildFileId, item))
                    buildFileId++
                } else {
                    disk.add(FreeSpace(item))
                }
                diskOffset += item

                //
                isFile = !isFile
            }
//        printDisk(disk)

        val maxFileId = buildFileId - 1
        for (fileId in maxFileId downTo 0) {
            println("\nFILE $fileId")
//            printDisk(disk)
//            println("- - - -")
            val fileIndex = disk.indexOfFirst { it is File && it.fileId == fileId }
            val file = disk[fileIndex] as File
            val itemIndexList = disk.mapIndexed { index, diskItem -> index to diskItem }
            val candidate = itemIndexList.firstOrNull { it.second is FreeSpace && it.second.size >= file.size }
            if (candidate != null && candidate.first <= fileIndex) {
                val (freeSpaceIndex, freeSpace) = candidate
                // 0 .. freeSpaceIndex-1, freeSpaceIndex, freeSpaceIndex+1 .. fileIndex-1, fileIndex, fileIndex + 1
                val toInsert =
                    if (freeSpace.size == file.size) {
                        listOf(file)
                    } else {
                        listOf(file, FreeSpace(freeSpace.size - file.size))
                    }
                disk =
                    (
                        disk.slice(0..(freeSpaceIndex - 1)) +
                            toInsert +
                            disk.slice((freeSpaceIndex + 1)..(fileIndex - 1)) +
                            FreeSpace(file.size) +
                            disk.slice((fileIndex + 1)..<disk.size)
                    ).toMutableList()
//                printDisk(disk)
//                println("Simplifying free space:")
                disk =
                    buildList {
                        var freeSpaceItem = FreeSpace(0)
                        disk.forEach { diskItem ->
                            when (diskItem) {
                                is File -> {
                                    if (freeSpaceItem.size > 0) {
                                        add(freeSpaceItem)
                                        freeSpaceItem = FreeSpace(0)
                                    }
                                    add(diskItem)
                                }
                                is FreeSpace -> freeSpaceItem = FreeSpace(freeSpaceItem.size + diskItem.size)
                            }
                        }
                        if (freeSpaceItem.size > 0) {
                            add(freeSpaceItem)
                        }
                    }.toMutableList()
//                printDisk(disk)
            }
        }

        val result =
            buildList {
                disk.forEach { item ->
                    when (item) {
                        is FreeSpace -> addAll(List(item.size) { -1 })
                        is File -> addAll(List(item.size) { item.fileId })
                    }
                }
            }

        return result
            .mapIndexed { index, fileId ->
                if (fileId > 0) {
                    index.toLong() * fileId.toLong()
                } else {
                    0
                }
            }.sum()
    }

    println("Day $DAY")
    val testInput = readInput("Day${DAY}_test")
    check(part1(testInput).also { println("Part1 output: $it") } == PART1_CHECK)
    check(part2(testInput).also { println("Part2 output: $it") } == PART2_CHECK)

    val input = readInput("Day$DAY")
    println("Part1 final output: ${part1(input)}")
    println("Part2 final output: ${part2(input)}")
}
