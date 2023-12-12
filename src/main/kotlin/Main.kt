import day.*
import kotlin.system.measureTimeMillis

fun main() {

    val days = listOf<Day>(

    )
    listOf<Day>(Day12).forEach(::solveDay)
}

private fun solveDay(day: Day) {
    println("########### ${day::class.simpleName} ###########")
    println("Part 1:")
    println(" (" + measureTimeMillis {
        print(
            """Example:  ${
                day.examplePartOne().apply {
                    check(this == day.examplePartOneSolution) {
                        "example part one should be ${day.examplePartOneSolution} but is $this"
                    }
                }
            }"""
        )
    } + "ms)")
    println(" (" + measureTimeMillis {
        print("Solution: ${day.solvePartOne()}")
    } + "ms)")
    println("")
    println("Part 2:")
    println(" (" + measureTimeMillis {
        print(
            """Example:  ${
                day.examplePartTwo().apply {
                    check(this == day.examplePartTwoSolution) {
                        "example part two should be ${day.examplePartTwoSolution} but is $this"
                    }
                }
            }"""
        )
    } + "ms)")
    println(" (" + measureTimeMillis {
        print("Solution: ${day.solvePartTwo()}")
    } + "ms)")
    println()
}