package day

import util.FileReader.getExampleSplitByEmptyLine
import util.FileReader.getInputSplitByEmptyLine
import util.lcm
import util.repeat
import util.toStringList


object Day08 : Day("08", "2", "6") {
    override fun examplePartOne() = getExampleSplitByEmptyLine(day).parseGraph().solve().toString()
    override fun examplePartTwo() = getExampleSplitByEmptyLine(day).parseGraph().solveAll().toString()
    override fun solvePartOne() = getInputSplitByEmptyLine(day).parseGraph().solve().toString()
    override fun solvePartTwo() = getInputSplitByEmptyLine(day).parseGraph().solveAll().toString()

    private fun List<String>.parseGraph(): DesertGraph {
        val (instructionString, subPathsString) = this
        return DesertGraph(instructionString.toStringList(), parseSubPaths(subPathsString))
    }

    private fun parseSubPaths(subPathsString: String) =
        subPathsString
            .split("\n").map { it.replace("""[(,)]""".toRegex(), "").split("""\s=\s""".toRegex()) }
            .associateBy(
                { it.first() },
                { it.last().split("""\s""".toRegex()).run { DesertCrossing(first(), last()) } }
            )

    data class DesertGraph(
        val instructions: List<String>,
        val subPaths: Map<String, DesertCrossing>
    ) {
        fun solve(
            start: String = "AAA",
            endCondition: (position: String) -> Boolean = { position -> position == "ZZZ" }
        ): Long = findPath(start).indexOfFirst { endCondition(it) }.toLong()

        fun solveAll(): Long {
            val solutions = subPaths.keys.filter { it.endsWith("A") }
                .map { start -> solve(start) { position -> position.endsWith("Z") } }
            return solutions.lcm()
        }

        private fun findPath(start: String) = instructions.asSequence().repeat()
            .scan(start) { acc: String, instruction: String -> subPaths.getValue(acc).chooseByInstruction(instruction) }
    }

    data class DesertCrossing(
        val left: String,
        val right: String,
    ) {
        fun chooseByInstruction(instruction: String) = when (instruction) {
            "L" -> left
            "R" -> right
            else -> error("only L and R are possible")
        }
    }
}
