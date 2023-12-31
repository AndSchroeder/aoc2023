package day

import day.Day10.PipeFieldType.*
import kotlin.math.min


object Day10 : Day("10", "22", "4") {
    override fun examplePartOne() = getExampleList().solveOne()
    override fun examplePartTwo() = getExampleList().solveTwo()
    override fun solvePartOne() = getInputList().solveOne()
    override fun solvePartTwo() = getInputList().solveTwo()

    private fun List<String>.solveOne() = parsePipeMaze().apply { traverse() }.maxValue().toString()
    private fun List<String>.solveTwo(): String {
        val maze = parsePipeMaze().apply { traverse() }
        return maze.countInside().toString()
    }

    private fun List<String>.parsePipeMaze() = PipeMaze(mapIndexed { y, line ->
        line.toList().mapIndexed { x, field ->
            parsePipeField(field, x, y)
        }
    })

    private fun parsePipeField(char: Char, x: Int, y: Int) = PipeField(type = PipeFieldType.from(char), x, y)

    data class PipeMaze(
        val grid: List<List<PipeField>>,
    ) {

        fun countInside(): Int {
            grid.flatten().filter { !it.visited }.forEach { field -> field.type = GROUND }
            val grounds = grid.flatten().filter { it.type == GROUND }

            grounds.forEach { field -> field.checkEnclosing(maze = this) }
            return grid.flatten().count { it.type == INSIDE }
        }

        fun maxValue() = grid.flatten().filter { it.value != Int.MAX_VALUE }.maxBy { it.value }.value

        tailrec fun traverse(stack: MutableList<PipeField> = mutableListOf(start)) {
            if (stack.isEmpty()) return
            val element = stack.removeFirst()
            element.visited = true
            val neighbors = element.neighbors(this).onEach { it.value = min(it.value, element.value + 1) }
            stack.addAll(neighbors)
            traverse(stack)
        }

        fun find(x: Int, y: Int) = if ((minX..maxX).contains(x) && (minY..maxY).contains(y)) grid[y][x] else null

        private val start get() = grid.flatten().first { field -> field.type == START }.apply { value = 0 }
        private val minX = 0
        private val minY = 0
        private val maxX = grid.first().size - 1
        private val maxY = grid.size - 1

        override fun toString() =
            grid.joinToString("\n") { line -> line.joinToString("") { field -> field.toString() } }
    }

    data class PipeField(
        var type: PipeFieldType,
        val x: Int,
        val y: Int,
        var value: Int = Int.MAX_VALUE,
        var visited: Boolean = false
    ) {

        fun neighbors(maze: PipeMaze) = when (type) {
            START -> getStartNeighbors(maze)
            GROUND -> listOf()
            else -> listOfNotNull(
                maze.find(x + type.prevX, y + type.prevY),
                maze.find(x + type.nextX, y + type.nextY),
            ).filter { !it.visited }
        }

        fun checkEnclosing(maze: PipeMaze) {
            val minToBorder = min(y, x) // count intersection for ray to upper left border
            val rayIntersections = (1..minToBorder).count { diff ->
                val type = maze.find(x - diff, y - diff)!!.type
                PipeFieldType.edgeTypes().contains(type)
            }

            type = if (rayIntersections % 2 == 0) OUTSIDE else INSIDE
        }

        private fun getStartNeighbors(maze: PipeMaze): List<PipeField> {
            val list = mutableListOf<PipeField>()
            val north = maze.find(x, y - 1)
            val east = maze.find(x + 1, y)
            val south = maze.find(x, y + 1)
            val west = maze.find(x - 1, y)
            if (north != null && listOf(VERTICAL, SOUTH_TO_WEST, SOUTH_TO_EAST).contains(north.type)) list.add(north)
            if (east != null && listOf(HORIZONTAL, SOUTH_TO_WEST, NORTH_TO_WEST).contains(east.type)) list.add(east)
            if (south != null && listOf(VERTICAL, NORTH_TO_EAST, NORTH_TO_WEST).contains(south.type)) list.add(south)
            if (west != null && listOf(HORIZONTAL, NORTH_TO_EAST, SOUTH_TO_EAST).contains(west.type)) list.add(west)
            return list.toList()
        }

        override fun toString() = type.toString()
    }

    enum class PipeFieldType(val char: Char, val prevX: Int, val prevY: Int, val nextX: Int, val nextY: Int) {
        VERTICAL('|', 0, -1, 0, 1),
        HORIZONTAL('-', -1, 0, 1, 0),
        NORTH_TO_EAST('L', 0, -1, 1, 0),
        NORTH_TO_WEST('J', -1, 0, 0, -1),
        SOUTH_TO_WEST('7', -1, 0, 0, 1),
        SOUTH_TO_EAST('F', 1, 0, 0, 1),
        GROUND('.', 0, 0, 0, 0),
        START('S', 0, 0, 0, 0),
        INSIDE('I', 0, 0, 0, 0),
        OUTSIDE('O', 0, 0, 0, 0);

        override fun toString() = char.toString()

        companion object {
            fun from(char: Char) = entries.first { it.char == char }

            fun edgeTypes() = listOf(NORTH_TO_WEST, SOUTH_TO_EAST, START, VERTICAL, HORIZONTAL)
        }
    }
}
