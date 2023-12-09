package day

import util.sum
import util.toLongList


object Day09 : Day("09", "114", "2") {
    override fun examplePartOne() = getExampleList().parseHistories().nextValue().toString()
    override fun examplePartTwo() = getExampleList().parseHistories().previousValue().toString()
    override fun solvePartOne() = getInputList().parseHistories().nextValue().toString()
    override fun solvePartTwo() = getInputList().parseHistories().previousValue().toString()

    private fun List<String>.parseHistories() = map { parseHistory(it) }
    private fun List<OasisHistory>.nextValue() = sumOf { it.nextValue() }
    private fun List<OasisHistory>.previousValue() = sumOf { it.previousValue() }

    private fun parseHistory(line: String) =
        OasisHistory(mutableListOf(OasisHistoryRow(line.toLongList().reversed()))).apply { fill() }

    data class OasisHistory(
        val rows: MutableList<OasisHistoryRow>
    ) {
        fun nextValue() = rows.sumOf { it.historyRow.first() }
        fun previousValue() =
            rows.mapIndexed { index, oasisHistoryRow ->
                oasisHistoryRow.historyRow.last() * if (index % 2 == 0) 1 else -1
            }.sum()

        fun fill() {
            while (!isDone()) {
                rows.add(OasisHistoryRow(rows.last().nextIteration()))
            }
        }

        private fun isDone() = rows.any { it.isDone() }
    }

    data class OasisHistoryRow(
        val historyRow: List<Long>
    ) {

        fun isDone() = historyRow.all { it == 0L }

        fun nextIteration() =
            historyRow.zipWithNext().map { (first, second) -> first - second }
    }

}
