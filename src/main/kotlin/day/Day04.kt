package day

import kotlin.math.pow


object Day04 : Day("04", "13", "30") {
    override fun examplePartOne() = getExampleList().createScatchCards().getScoreSum()
    override fun examplePartTwo() = getExampleList().createScatchCards().getAmountSum()
    override fun solvePartOne() = getInputList().createScatchCards().getScoreSum()
    override fun solvePartTwo() = getInputList().createScatchCards().getAmountSum()

    private fun List<ScratchCard>.getScoreSum() = sumOf { it.getScore() }.toString()
    private fun List<ScratchCard>.getAmountSum(): String {
        forEachIndexed { startIndex, card -> copyForCard(startIndex, card) }
        return sumOf { it.amount }.toString()
    }

    private fun List<ScratchCard>.copyForCard(startIndex: Int, card: ScratchCard) {
        (startIndex + 1..startIndex + card.matchingSize()).forEach { index ->
            increaseAmount(index, card)
        }
    }

    private fun List<ScratchCard>.increaseAmount(index: Int, card: ScratchCard) {
        find { it.index == index }?.increaseAmount(card)
    }

    private fun List<String>.createScatchCards() = mapIndexed { index, line -> createScratchCard(index, line) }
    private fun createScratchCard(index: Int, line: String): ScratchCard {
        val (winningNumbersString, cardNumbersString) = getNumbers(line)
        return ScratchCard(winningNumbersString.toIntList(), cardNumbersString.toIntList(), index)
    }

    private fun getNumbers(line: String) = line.replace("""Card\s+\d+:\s+""".toRegex(), "")
        .split("""\s+\|\s+""".toRegex())

    data class ScratchCard(
        val winningNumbers: List<Int> = emptyList(),
        val cardNumbers: List<Int> = emptyList(),
        val index: Int,
        var amount: Long = 1
    ) {
        fun increaseAmount(other: ScratchCard) {
            amount += other.amount
        }

        fun getScore() = if (matchingSize() == 0) 0 else 2.pow(matchingSize() - 1)

        fun matchingSize() = matchingCardNumbers().size
        private fun matchingCardNumbers() = cardNumbers.filter { winningNumbers.contains(it) }
    }

    private fun Int.pow(other: Int) = this.toDouble().pow(other.toDouble()).toInt()
    private fun String.toIntList() = split("""\s+""".toRegex()).map(String::toInt)
}
