package day

import util.FileReader

abstract class Day(
    val day: String,
    val examplePartOneSolution: String,
    val examplePartTwoSolution: String
) {
    abstract fun examplePartOne(): String
    abstract fun examplePartTwo(): String
    abstract fun solvePartOne(): String
    abstract fun solvePartTwo(): String

    fun getExampleList() = FileReader.getExampleList(day)
    fun getInputList() = FileReader.getInputList(day)
}