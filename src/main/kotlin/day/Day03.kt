package day

import kotlin.math.max


object Day03 : Day("03", "4361", "467835") {
    override fun examplePartOne() = findPartSum(getExampleList())
    override fun examplePartTwo() = findGearSum(getExampleList())
    override fun solvePartOne() = findPartSum(getInputList())
    override fun solvePartTwo() = findGearSum(getInputList())

    private fun findPartSum(input: List<String>) = createPartGrid(input).getValidPartNumbersSum().toString()
    private fun findGearSum(input: List<String>) = createPartGrid(input).getGearRatioSum().toString()

    private fun createPartGrid(input: List<String>): PartGrid {
        val partNumbers: MutableList<PartGrid.PartNumber> = mutableListOf()
        val partSymbols: MutableList<PartGrid.PartSymbol> = mutableListOf()
        input.forEachIndexed { y, line -> handleLine(line, y, partNumbers, partSymbols) }
        return PartGrid(partNumbers, partSymbols)
    }

    private fun handleLine(
        line: String,
        y: Int,
        partNumbers: MutableList<PartGrid.PartNumber>,
        partSymbols: MutableList<PartGrid.PartSymbol>
    ) {
        var xStart: Int? = null
        var number: String = ""
        (line + ".").toStringList().forEachIndexed() { x, value ->
            if (value.matches("""\d""".toRegex())) {
                if (xStart == null) {
                    xStart = x
                }
                number += value
            } else {
                if (xStart != null) {
                    partNumbers.add(PartGrid.PartNumber(xStart!!, x - 1, y, number.toInt()))
                    xStart = null
                    number = ""
                }
                if (value != ".") {
                    partSymbols.add(PartGrid.PartSymbol(x, y, value))
                }
            }
        }
    }

    private fun String.toStringList() = toList().map(Char::toString)


    data class PartGrid(
        val partNumbers: List<PartNumber>,
        val partSymbols: List<PartSymbol>,
    ) {

        fun getValidPartNumbersSum() = getValidNumbers().sumOf { it.value.toLong() }

        fun getGearRatioSum() =
            partSymbols.filter { it.isGear() }.map { it.gearRatioNumber(partNumbers) }.sumOf { it.toLong() }

        private fun getValidNumbers() = partNumbers.filter { it.hasPartSymbolNeighbor(partSymbols) }

        data class PartSymbol(
            val x: Int,
            val y: Int,
            val value: String,
        ) {
            fun findByCoordinate(x: Int, y: Int) = this.x == x && this.y == y

            fun isGear() = value == "*"

            fun gearRatioNumber(partNumbers: List<PartNumber>): Int {
                val numberNeighbors = numberNeighbors(partNumbers).distinct()
                return if (numberNeighbors.size == 2) numberNeighbors[0].value * numberNeighbors[1].value else 0
            }

            private fun numberNeighbors(partNumbers: List<PartNumber>): List<PartNumber> {
                val xMin = max(0, x - 1)
                val yMin = max(0, y - 1)

                return (xMin..x + 1).flatMap() { searchX ->
                    (yMin..y + 1).flatMap() { searchY ->
                        partNumbers.filter { it.findByCoordinate(searchX, searchY) }
                    }
                }
            }
        }

        data class PartNumber(
            val xStart: Int,
            val xEnd: Int,
            val y: Int,
            val value: Int
        ) {
            fun findByCoordinate(x: Int, y: Int) = (this.xStart <= x && this.xEnd >= x && this.y == y)

            fun hasPartSymbolNeighbor(symbols: List<PartSymbol>): Boolean {
                val xMin = max(0, xStart - 1)
                val yMin = max(0, y - 1)

                return (xMin..xEnd + 1).any() { searchX ->
                    (yMin..y + 1).any() { searchY ->
                        symbols.any { it.findByCoordinate(searchX, searchY) }
                    }
                }
            }
        }
    }
}
