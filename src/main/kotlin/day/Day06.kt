package day


object Day06 : Day("06", "288", "71503") {
    override fun examplePartOne() = getExampleList().createRaces().getWinningAmountProduct()
    override fun examplePartTwo() = getExampleList().createRace().getWinningAmount().toString()
    override fun solvePartOne() = getInputList().createRaces().getWinningAmountProduct()
    override fun solvePartTwo() = getInputList().createRace().getWinningAmount().toString()


    private fun List<String>.createRaces(): List<Race> {
        val times = getTimeRaw().split("""\s+""".toRegex()).toLongList()
        val distances = getDistanceRaw().split("""\s+""".toRegex()).toLongList()
        return times.zip(distances).map { (time, distance) -> Race(time, distance) }
    }

    private fun List<String>.createRace(): Race {
        val time = getTimeRaw().replace("""\s+""".toRegex(), "").toLong()
        val distance = getDistanceRaw().replace("""\s+""".toRegex(), "").toLong()
        return Race(time, distance)
    }

    private fun List<String>.getDistanceRaw() = this.last().replace("""Distance:\s+""".toRegex(), "")

    private fun List<String>.getTimeRaw() = this.first().replace("""Time:\s+""".toRegex(), "")

    private fun List<Race>.getWinningAmountProduct() = fold(1L) {acc, race -> acc * race.getWinningAmount()}.toString()

    data class Race(
        val time: Long,
        val distance: Long,
    ) {
        fun getWinningAmount(): Long {
            val firstWinner = (1..(time + 1) / 2).find { passed -> (time - passed) * passed > distance}
            return firstWinner?.let { time - firstWinner * 2 + 1 } ?: 0
        }
    }

    private fun List<String>.toLongList() = map { it.toLong() }

}
