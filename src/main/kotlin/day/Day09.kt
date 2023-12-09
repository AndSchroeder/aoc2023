package day

import util.toLongList


object Day09 : Day("09", "114", "6") {
    override fun examplePartOne() = getExampleList().parseHistories().nextValue().toString()
    override fun examplePartTwo() = "getExampleList()"
    override fun solvePartOne() = getInputList().parseHistories().nextValue().toString()
    override fun solvePartTwo() = "getExampleList()"

    private fun List<String>.parseHistories() = map { parseHistory(it) }
    private fun List<OasisHistory>.nextValue() = sumOf { it.nextValue() }

    private fun parseHistory(line: String) =
        OasisHistory(mutableListOf(OasisHistoryRow(line.toLongList().reversed()))).apply { fill() }

    data class OasisHistory(
        val rows: MutableList<OasisHistoryRow>
    ) {
        fun nextValue() = rows.sumOf { it.historyRow.first() }

        fun isDone() = rows.any { it.isDone() }

        fun fill() {
            while (!isDone()) {
                rows.add(OasisHistoryRow(rows.last().nextIteration()))
            }
        }
    }

    data class OasisHistoryRow(
        val historyRow: List<Long>
    ) {

        fun isDone() = historyRow.all { it == 0L }

        fun nextIteration() =
            historyRow.zipWithNext().map { (first, second) -> first - second }
    }

}
