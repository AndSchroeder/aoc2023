package day

import util.toIntList


object Day12 : Day("12", "21", "") {
    override fun examplePartOne() = getExampleList().parseSprings().solveOne()
    override fun examplePartTwo() = "getExampleList()"
    override fun solvePartOne() = getInputList().parseSprings().solveOne()
    override fun solvePartTwo() = "getInputList()"

    private fun List<String>.parseSprings() = map { line ->
        val (springString, damagedString) = line.split(" ")
        SpringProblem(("$springString.").toList().map { Spring.from(it) }, damagedString.split(",").toIntList())
    }

    private fun List<SpringProblem>.solveOne() = sumOf { it.getCombinations() }.toString()

    data class SpringProblem(
        val springs: List<Spring>,
        val damagedSpringAmounts: List<Int>
    ) {

        fun getCombinations(springIndex: Int = 0, amountIndex: Int = 0): Int {
            var combinations = 0
            if (springIndex >= springs.size) {
                return if (amountIndex == damagedSpringAmounts.size) 1 else 0
            }
            val currentSpring = springs[springIndex]
            if (currentSpring.isMaybeWorking()) combinations += getCombinations(springIndex + 1, amountIndex)
            if (amountIndex == damagedSpringAmounts.size) return combinations
            val currentAmount = damagedSpringAmounts[amountIndex]
            if (foundGroup(springIndex, currentAmount)) {
                combinations += getCombinations(springIndex + currentAmount + 1, amountIndex + 1)
            }
            return combinations
        }

        private fun foundGroup(springIndex: Int, currentAmount: Int) =
            hasEnoughRoomForGroup(springIndex, currentAmount) &&
                    hasOnlyMaybeBrokenInGroup(springIndex, springIndex + currentAmount) &&
                    isGroupEnding(springIndex, currentAmount)

        private fun isCurrentSpringMaybeBroken(currentSpring: Spring) = currentSpring.isMaybeBroken()

        private fun isGroupEnding(springIndex: Int, currentAmount: Int) =
            springs[springIndex + currentAmount].isMaybeWorking()

        private fun hasEnoughRoomForGroup(springIndex: Int, currentAmount: Int) =
            springIndex + currentAmount < springs.size

        private fun hasOnlyMaybeBrokenInGroup(startIndex: Int, endIndex: Int) =
            springs.subList(startIndex, endIndex).all { isCurrentSpringMaybeBroken(it) }
    }

    enum class Spring(val char: Char) {
        WORKING('.'),
        BROKEN('#'),
        UNKNOWN('?');

        fun isMaybeWorking() = listOf(WORKING, UNKNOWN).contains(this)
        fun isMaybeBroken() = listOf(BROKEN, UNKNOWN).contains(this)

        companion object {
            fun from(char: Char) = entries.first { char == it.char }
        }
    }
}
