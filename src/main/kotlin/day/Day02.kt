package day

import kotlin.math.max


object Day02 : Day("02", "8", "2286") {
    private const val BLUE_MAX = 14
    private const val GREEN_MAX = 13
    private const val RED_MAX = 12

    override fun examplePartOne() = getExampleList().readCubesByMax().toString()
    override fun examplePartTwo() = getExampleList().readCubesByPower().toString()
    override fun solvePartOne() = getInputList().readCubesByMax().toString()
    override fun solvePartTwo() = getInputList().readCubesByPower().toString()

    private fun List<String>.readCubesByMax() = map(::readLine).sumByMax()
    private fun List<String>.readCubesByPower() = map(::readLine).sumOf { it.power() }

    private fun readLine(line: String) = line.split(";").fold(CubeSet()) { acc, game ->
        acc.getMaxedCube(
            blue = game.getAmountOf("blue"),
            green = game.getAmountOf("green"),
            red = game.getAmountOf("red"),
        )
    }

    private fun List<CubeSet>.sumByMax() = mapIndexed { index, cubeSet -> valueByBorder(cubeSet, index) }.sum()

    private fun valueByBorder(cubeSet: CubeSet, index: Int) = if (cubeSet.inBorders()) index + 1 else 0

    private fun String.getAmountOf(color: String) =
        split(",").find { cubes -> cubes.contains(color) }?.clearAmount() ?: 0

    private fun String.clearAmount() = replace("""(Game\s\d+)|(\D|\s)+""".toRegex(), "").toInt()

    data class CubeSet(
        val blue: Int = 0,
        val green: Int = 0,
        val red: Int = 0,
    ) {
        fun getMaxedCube(blue: Int, green: Int, red: Int) = this.copy(
            blue = max(this.blue, blue),
            green = max(this.green, green),
            red = max(this.red, red),
        )

        fun power() = blue * red * green

        fun inBorders() = this.blue <= BLUE_MAX && this.green <= GREEN_MAX && this.red <= RED_MAX
    }
}
