import day.*

fun main() {

    val days = listOf<Day>(

    )
    listOf<Day>(Day01).forEach(::solveDay)
}

private fun solveDay(day: Day) {
    println("########### ${day::class.simpleName} ###########")
    println("Part 1:")
    println(
        """Example:  ${
            day.examplePartOne().apply {
                check(this == day.examplePartOneSolution) { 
                    "example part one should be ${day.examplePartOneSolution} but is $this" 
                }
            }
        }"""
    )
    println("Solution: ${day.solvePartOne()}")
    println("")
    println("Part 2:")
    println(
        """Example:  ${
            day.examplePartTwo().apply {
                check(this == day.examplePartTwoSolution) { 
                    "example part one should be ${day.examplePartTwoSolution} but is $this" 
                }
            }
        }"""
    )
    println("Solution: ${day.solvePartTwo()}")
    println()
}