package day

import util.combinations
import util.toStringList
import kotlin.math.max
import kotlin.math.min


object Day11 : Day("11", "374", "") {
    override fun examplePartOne() = getExampleList().solveOne()
    override fun examplePartTwo() = "getExampleList()"
    override fun solvePartOne() = getInputList().solveOne()
    override fun solvePartTwo() = "getInputList()"

    private fun List<String>.solveOne() = ElfUniverse.parse(this).getDistances().sum().toString()

    data class ElfUniverse(
        val galaxies: List<ElfGalaxy>,
        val xExpansions: Set<Int>,
        val yExpansions: Set<Int>,
    ) {

        fun getDistances() =
            galaxies.combinations().map { (first, second) -> first.getDistance(second, xExpansions, yExpansions) }

        companion object {
            fun parse(input: List<String>): ElfUniverse {
                val grid = input.map(String::toStringList)
                var yExpansions = mutableSetOf<Int>()
                var xExpansions = mutableSetOf<Int>()
                val galaxies = grid.mapIndexed { y, line ->
                    if (!line.contains("#")) yExpansions.add(y)
                    line.mapIndexed { x, field -> if (field == "#") ElfGalaxy(x, y) else null }.filterNotNull()
                }.flatten()
                (0..<grid.first().size).forEach { x ->
                    val xLine = List(grid.size) { y -> grid[y][x] }
                    if (!xLine.contains("#")) xExpansions.add(x)
                }
                return ElfUniverse(galaxies, xExpansions, yExpansions)
            }


        }
    }

    data class ElfGalaxy(val x: Int, val y: Int) {

        fun getDistance(other: ElfGalaxy, xExpansions: Set<Int>, yExpansions: Set<Int>): Int {
            val minX = min(x, other.x)
            val maxX = max(x, other.x)
            val minY = min(y, other.y)
            val maxY = max(y, other.y)
            val expandX = (minX..maxX).intersect(xExpansions).size
            val expandY = (minY..maxY).intersect(yExpansions).size
            return maxX - minX + expandX + maxY - minY + expandY
        }
    }
}
