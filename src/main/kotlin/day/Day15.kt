package day

import util.FileReader.getExampleRaw
import util.FileReader.getInputRaw

object Day15 : Day("15", "1320", "145") {
    override fun examplePartOne() = getExampleRaw(day).parseInputPartOne().sumOf { it.hashCode() }.toString()
    override fun examplePartTwo() = getExampleRaw(day).parseInputPartTwo()
    override fun solvePartOne() = getInputRaw(day).parseInputPartOne().sumOf { it.hashCode() }.toString()
    override fun solvePartTwo() = getInputRaw(day).parseInputPartTwo()

    private fun String.parseInputPartOne() = split(',').map(::InitializationInput)
    private fun String.parseInputPartTwo() =
        split(',').apply { LabelInput.initMap() }.map(::LabelInput).onEach { LabelInput.put(it) }
            .let { LabelInput.score() }.toString()

    data class InitializationInput(val input: String) {
        override fun hashCode() = input.fold(0) { acc, char ->
            ((acc + char.code) * 17) % 256
        }
    }

    data class LabelInput(val input: String) {

        override fun hashCode() = getLabel().fold(0) { acc, char ->
            ((acc + char.code) * 17) % 256
        }

        private fun splited() = input.split("""([-=])""".toRegex())

        fun getLabel() = splited().first()
        fun getVocalIndex() = if (input.contains("=")) splited().last().toInt() else 0

        companion object {

            val map = mutableMapOf<Int, MutableList<Pair<String, Int>>>()

            fun initMap() = (0..255).forEach { map[it] = mutableListOf() }

            fun put(input: LabelInput) {
                val hashCode = input.hashCode()
                val label = input.getLabel()
                val vocalIndex = input.getVocalIndex()

                map[hashCode]?.apply {
                    val index = indexOfFirst { it.first == label }

                    if (input.input.contains("-")) {
                        if (index != -1) removeAt(index)
                    } else {
                        if (index != -1) set(index, label to vocalIndex) else add(label to vocalIndex)
                    }
                }
            }

            fun score() = map.toList().sumOf { (hashIndex, list) ->
                list.mapIndexed { entryIndex, (_, vocalValue) -> (hashIndex + 1) * (entryIndex + 1) * vocalValue }.sum()
            }
        }
    }
}
