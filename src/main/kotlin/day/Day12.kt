package day

import util.toIntList


object Day12 : Day("12", "21", "525152") {
    override fun examplePartOne() = getExampleList().parseSprings().solve()
    override fun examplePartTwo() = getExampleList().parseSprings(true).solve()
    override fun solvePartOne() = getInputList().parseSprings().solve()
    override fun solvePartTwo() = getInputList().parseSprings(true).solve()

    private fun List<String>.parseSprings(multiply: Boolean = false) = map { line ->
        val (springString, damagedString) = line.split(" ")
        val readySpringString = if (!multiply) springString else List(5) { springString }.joinToString("?")
        val readyDamagedString = if (!multiply) damagedString else List(5) {damagedString}.joinToString(",")
        SpringProblem(
            ("$readySpringString.").toList().map { Spring.from(it) },
            readyDamagedString.split(",").toIntList()
        )
    }

    private fun List<SpringProblem>.solve() = sumOf { it.getCombinations() }.toString()

    data class SpringProblem(
        val springs: List<Spring>,
        val damagedSpringAmounts: List<Int>
    ) {
        private val memory = mutableMapOf<Pair<Int, Int>, Long>()

        fun getCombinations(springIndex: Int = 0, amountIndex: Int = 0): Long {
            if (hasSpringIndexReachedEnd(springIndex)) {
                return if (hasAmountIndexReachedEnd(amountIndex)) 1 else 0
            }

            val currentSpring = springs[springIndex]
            var combinations = if (currentSpring.isMaybeWorking()) getMemory(springIndex + 1, amountIndex) else 0

            if (isAmountInBounds(amountIndex)) {
                val currentAmount = damagedSpringAmounts[amountIndex]
                if (foundGroup(springIndex, currentAmount)) {
                    combinations += getMemory(springIndex + currentAmount + 1, amountIndex + 1)
                }
            }
            return combinations
        }

        private fun hasAmountIndexReachedEnd(amountIndex: Int) = amountIndex == damagedSpringAmounts.size

        private fun hasSpringIndexReachedEnd(springIndex: Int) = springIndex >= springs.size

        private fun isAmountInBounds(amountIndex: Int) = amountIndex < damagedSpringAmounts.size

        private fun getMemory(springIndex: Int, amountIndex: Int) = memory.getOrPut(springIndex to amountIndex) {
            getCombinations(springIndex, amountIndex)
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
