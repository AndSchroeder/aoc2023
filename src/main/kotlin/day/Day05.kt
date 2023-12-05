package day


object Day05 : Day("05", "35", "46") {
    override fun examplePartOne() = getMinLocationPartOne(getExampleList())
    override fun examplePartTwo() = getMinLocationPartTwo(getExampleList())
    override fun solvePartOne() = getMinLocationPartOne(getInputList())
    override fun solvePartTwo() = getMinLocationPartTwo(getInputList())

    private fun getMinLocationPartOne(input: List<String>): String {
        val (converterMaps, firstLine) = parseMapAndGetSeedLine(input)
        val seeds = firstLine.replace("seeds: ", "").toLongList()
        return getMinimumLocation(converterMaps, seeds)
    }

    private fun getMinLocationPartTwo(input: List<String>): String {
        val (converterMaps, firstLine) = parseMapAndGetSeedLine(input)
        val seedRanges = firstLine.replace("seeds: ", "").toLongList()
        val min = seedRanges.chunked(2).minOf { seeds ->
            val range = (seeds[0]..<seeds[0] + seeds[1])
            range.minOf { convertThroughChain(it, converterMaps) }
        }
        return min.toString()
    }

    private fun parseMapAndGetSeedLine(input: List<String>): Pair<List<ConverterMap>, String> {
        val inputByEmptyLine = input.joinToString("\n").split("""\n\n""".toRegex())
        val converterInput = inputByEmptyLine.cutFirst()
        val converterMaps = parseConverterMaps(converterInput)
        val firstLine = inputByEmptyLine[0]
        return Pair(converterMaps, firstLine)
    }

    private fun getMinimumLocation(
        converterMaps: List<ConverterMap>,
        seeds: List<Long>
    ): String {
        val locations = seeds.map { convertThroughChain(it, converterMaps) }
        return locations.minOfOrNull { it }.toString()
    }

    private fun convertThroughChain(number: Long, converterMaps: List<ConverterMap>) =
        converterMaps.fold(number) { acc, converterMap -> converterMap.convert(acc) }

    private fun parseConverterMaps(converterInput: List<String>) = converterInput.map {
        val numberInputs = it.split("\n")
        val converters = parseConverters(numberInputs)
        ConverterMap(converters)
    }

    private fun parseConverters(numberInputs: List<String>) = numberInputs.cutFirst().map {
        val splitNumbers = it.toLongList()
        NumberConverter(sourceStart = splitNumbers[1], destinationStart = splitNumbers[0], length = splitNumbers[2])
    }

    data class ConverterMap(
        val converters: List<NumberConverter>
    ) {
        fun convert(number: Long): Long {
            val converter = converters.find { it.contains(number) }
            return converter?.convert(number) ?: number
        }
    }

    data class NumberConverter(
        val sourceStart: Long,
        val destinationStart: Long,
        val length: Long
    ) {
        fun contains(number: Long) = number >= sourceStart && number < sourceStart + length

        fun convert(number: Long) = number - sourceStart + destinationStart
    }

    private fun List<String>.cutFirst() = subList(1, this.size)
    private fun String.toLongList() = split(" ").map { it.toLong() }
}
