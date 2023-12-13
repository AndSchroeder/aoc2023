package day

import util.FileReader.getExampleSplitByEmptyLine
import util.FileReader.getInputSplitByEmptyLine
import kotlin.math.min


object Day13 : Day("13", "405", "") {
    override fun examplePartOne() = getExampleSplitByEmptyLine(day).parseInput().solve()
    override fun examplePartTwo() = "getExampleList()"
    override fun solvePartOne() = getInputSplitByEmptyLine(day).parseInput().solve()
    override fun solvePartTwo() = "getInputList()"

    fun List<String>.parseInput() = map { gridInput ->
        val fields = gridInput.split("\n").mapIndexed {y, lineInput ->
            lineInput.toList().mapIndexed { x, fieldInput ->
                MirrorField(x, y, MirrorFieldType.from(fieldInput))
            }
        }.flatten()
        MirrorGrid(fields)
    }

    private fun List<MirrorGrid>.solve() = onEach { it.setMirrors() }.sumOf { it.score() }.toString()

    data class MirrorGrid(
        val fields: List<MirrorField>,
        var xMirror: Int = 0,
        var yMirror: Int = 0,
        var xMirrorFixed: Int = 0,
        var yMirrorFixed: Int = 0
    ) {

        private val maxX = fields.maxOf { it.x }
        private val maxY = fields.maxOf { it.y }

        private val rows = fields.groupBy { it.y }
        private val cols = fields.groupBy { it.x }

        fun score() = xMirror + yMirror * 100

        fun setMirrors(): Pair<Int, Int> {
            getMirrors(0, maxX) { cols[it]!! }
            getMirrors(1, maxY) { rows[it]!! }
            return xMirror to yMirror
        }

        private fun getMirrors(axis: Int, max: Int, getAxisData: (Int) -> List<MirrorField>) {
            val visited = mutableListOf<MutableList<MirrorField>>()

            (0..max).forEach { i ->
                val data = getAxisData(i)!!

                if (visited.isNotEmpty() && visited.lastOrNull()?.asText() == data.asText()) {
                    val next = (i..max).map { j -> getAxisData(j) }
                    val last = visited.reversed()
                    val size = min(next.size, last.size)

                    if (next.subList(0, size).asText() == last.subList(0, size).asText()) {
                        if (axis == 0) xMirror = visited.size else yMirror = visited.size
                        return@forEach
                    }
                }
                visited.add(data.toMutableList())
            }
        }

        fun List<List<MirrorField>>.asText() = this.map { it.asText()}
        fun List<MirrorField>.asText() = this.map { it.type.char }.joinToString("")

        fun String.almostTheSame(other: String) = compareSimilarity(other) == 1
        fun String.compareSimilarity(other: String) = this.zip(other).count {(first, second) -> first != second}
    }

    data class MirrorField(val x: Int, val y: Int, val type: MirrorFieldType)

    enum class MirrorFieldType(val char: Char) {
        SAND('.'),
        ROCK('#');

        companion object {
            fun from(char: Char) = entries.first { it.char == char }
        }
    }
}
