package day

import util.combinations
import util.toStringList
import kotlin.math.max
import kotlin.math.min


object Day11 : Day("11", "374", "8410") {
    override fun examplePartOne() = getExampleList().solveOne()
    override fun examplePartTwo() = getExampleList().solveTwo(100)
    override fun solvePartOne() = getInputList().solveOne()
    override fun solvePartTwo() = getInputList().solveTwo(1_000_000)

    private fun List<String>.solveOne() = ElfUniverse.parse(this).getDistances().sum().toString()

    private fun List<String>.solveTwo(expansionFactor: Long) =
        ElfUniverse.parse(this, expansionFactor).getDistances().sum().toString()

    data class ElfUniverse(
        val galaxies: List<ElfGalaxy>,
        val xExpansions: Set<Int>,
        val yExpansions: Set<Int>,
        val expansionFactor: Long
    ) {

        fun getDistances() =
            galaxies.combinations().map { (first, second) -> first.getDistance(second, this) }

        companion object {
            fun parse(input: List<String>, expansionFactor: Long = 2L): ElfUniverse {
                val grid = input.map(String::toStringList)
                val yExpansions = mutableSetOf<Int>()
                val xExpansions = mutableSetOf<Int>()
                val galaxies = grid.mapIndexed { y, line ->
                    if (!line.contains("#")) yExpansions.add(y)
                    line.mapIndexed { x, field -> if (field == "#") ElfGalaxy(x, y) else null }.filterNotNull()
                }.flatten()
                (0..<grid.first().size).forEach { x ->
                    val xLine = List(grid.size) { y -> grid[y][x] }
                    if (!xLine.contains("#")) xExpansions.add(x)
                }
                return ElfUniverse(galaxies, xExpansions, yExpansions, expansionFactor)
            }
        }
    }

    data class ElfGalaxy(val x: Int, val y: Int) {

        fun getDistance(other: ElfGalaxy, universe: ElfUniverse): Long {
            val minX = min(x, other.x)
            val maxX = max(x, other.x)
            val minY = min(y, other.y)
            val maxY = max(y, other.y)
            val expandX = (minX..maxX).intersect(universe.xExpansions).size * (universe.expansionFactor - 1)
            val expandY = (minY..maxY).intersect(universe.yExpansions).size * (universe.expansionFactor - 1)
            return maxX - minX + expandX + maxY - minY + expandY
        }
    }
}
