package day

import util.FileReader.getExampleSplitByEmptyLine
import util.FileReader.getInputSplitByEmptyLine
import kotlin.math.min


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
        fun scoreFixed() = xMirrorFixed + yMirrorFixed * 100

        fun setMirrors() {
            setMirror(true, maxX, false) { cols[it]!! }
            setMirror(false, maxY, false) { rows[it]!! }
            setMirror(true, maxX, true) { cols[it]!! }
            setMirror(false, maxY, true) { rows[it]!! }
        }

        private fun setMirror(
            useXAxis: Boolean,
            max: Int,
            similarityCheck: Boolean,
            getAxisData: (Int) -> List<MirrorField>
        ) {
            val visited = mutableListOf<List<MirrorField>>()

            (0..max).forEach { i ->
                val data = getAxisData(i)

                if (visited.isNotEmpty()) {
                    val next = (i..max).map { j -> getAxisData(j) }
                    val last = visited.reversed()
                    val size = min(next.size, last.size)

                    val matchesMirror = if (similarityCheck) {
                        next.subList(0, size).almostTheSame(last.subList(0, size))
                    } else {
                        next.subList(0, size) == last.subList(0, size)
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

        private fun List<List<MirrorField>>.almostTheSame(other: List<List<MirrorField>>) = compareSimilarity(other) == 1
        private fun List<List<MirrorField>>.compareSimilarity(other: List<List<MirrorField>>) = this.flatten().zip(other.flatten()).count { (first, second) -> first != second }
    }

    data class MirrorField(val x: Int, val y: Int, val type: MirrorFieldType) {
        override fun equals(other: Any?): Boolean {
            return if (other is MirrorField) {
                this.type.equals(other.type)
            } else {
                super.equals(other)
            }
        }

        override fun toString() = type.char.toString()
        override fun hashCode(): Int {
            var result = x
            result = 31 * result + y
            result = 31 * result + type.hashCode()
            return result
        }
    }

    enum class MirrorFieldType(val char: Char) {
        SAND('.'),
        ROCK('#');

        companion object {
            fun from(char: Char) = entries.first { it.char == char }
        }
    }
}
