package day

import util.FileReader.getExampleSplitByEmptyLine
import util.FileReader.getInputSplitByEmptyLine
import kotlin.math.min
import kotlin.Int as Int1


object Day13 : Day("13", "405", "400") {
    override fun examplePartOne() = getExampleSplitByEmptyLine(day).parseInput().solveOne()
    override fun examplePartTwo() = getExampleSplitByEmptyLine(day).parseInput().solveTwo()
    override fun solvePartOne() = getInputSplitByEmptyLine(day).parseInput().solveOne()
    override fun solvePartTwo() = getInputSplitByEmptyLine(day).parseInput().solveTwo()

    private fun List<String>.parseInput() = map { gridInput ->
        val fields = gridInput.split("\n").mapIndexed { y, lineInput ->
            lineInput.toList().mapIndexed { x, fieldInput ->
                MirrorField(x, y, MirrorFieldType.from(fieldInput))
            }
        }.flatten()
        MirrorGrid(fields).apply { setMirrors() }
    }

    private fun List<MirrorGrid>.solveOne() = sumOf { it.score() }.toString()
    private fun List<MirrorGrid>.solveTwo() = sumOf { it.scoreFixed() }.toString()

    data class MirrorGrid(
        val fields: List<MirrorField>,
        var xMirror: Int1 = 0,
        var yMirror: Int1 = 0,
        var xMirrorFixed: Int1 = 0,
        var yMirrorFixed: Int1 = 0
    ) {

        private val maxX = fields.maxOf { it.x }
        private val maxY = fields.maxOf { it.y }

        private val rows = fields.groupBy { it.y }
        private val cols = fields.groupBy { it.x }

        fun score() = xMirror + yMirror * 100
        fun scoreFixed() = xMirrorFixed + yMirrorFixed * 100

        fun setMirrors() {
            setMirror(true, maxX, false) { cols[it]!! }
            setMirror(false, maxY, false) { rows[it]!! }
            setMirror(true, maxX, true) { cols[it]!! }
            setMirror(false, maxY, true) { rows[it]!! }
        }

        private fun setMirror(
            useXAxis: Boolean,
            max: Int1,
            similarityCheck: Boolean,
            getAxisData: (Int1) -> List<MirrorField>
        ) {
            val visited = mutableListOf<List<MirrorField>>()

            (0..max).forEach { i ->
                val data = getAxisData(i)!!

                if (visited.isNotEmpty()) {
                    val next = (i..max).map { j -> getAxisData(j) }
                    val last = visited.reversed()
                    val size = min(next.size, last.size)

                    val canHaveSimilarity = similarityCheck && next.subList(0, size).asTexts().almostTheSame(last.subList(0, size).asTexts())
                    val matchesMirror = if (similarityCheck) {
                        canHaveSimilarity
                    } else {
                        next.subList(0, size).asTexts() == last.subList(0, size).asTexts()
                    }

                    if (saveMirror(matchesMirror, similarityCheck, useXAxis, visited)) return@forEach
                }
                visited.add(data)
            }
        }

        private fun saveMirror(
            matchesMirror: Boolean,
            similarityCheck: Boolean,
            useXAxis: Boolean,
            visited: MutableList<List<MirrorField>>
        ): Boolean {
            if (matchesMirror) {
                if (similarityCheck) {
                    val mirrorFixed = if (useXAxis) xMirrorFixed else yMirrorFixed
                    val mirror = if (useXAxis) xMirror else yMirror

                    if (mirror != visited.size && mirrorFixed == 0) {
                        if (useXAxis) xMirrorFixed = visited.size else yMirrorFixed = visited.size
                        return true
                    }
                } else {
                    if (useXAxis) xMirror = visited.size else yMirror = visited.size
                    return true
                }
            }
            return false
        }

        private fun List<List<MirrorField>>.asTexts() = this.joinToString("") { it.asText() }
        private fun List<MirrorField>.asText() = this.map { it }.joinToString("")

        private fun String.almostTheSame(other: String) = compareSimilarity(other) == 1
        private fun String.compareSimilarity(other: String) = this.zip(other).count { (first, second) -> first != second }
    }

    data class MirrorField(val x: Int1, val y: Int1, val type: MirrorFieldType) {
        override fun toString() = type.char.toString()
    }

    enum class MirrorFieldType(val char: Char) {
        SAND('.'),
        ROCK('#');

        companion object {
            fun from(char: Char) = entries.first { it.char == char }
        }
    }
}
