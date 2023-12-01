package day


object Day01 : Day("01", "142", "281") {
    override fun examplePartOne() = getSum(exampleOne.split("\n"))
    override fun examplePartTwo() = getSum(exampleTwo.split("\n").replaceWordsWithDigitsForList())
    override fun solvePartOne() = getSum(getInputList())
    override fun solvePartTwo() = getSum(getInputList().replaceWordsWithDigitsForList())



    private fun List<String>.replaceWordsWithDigitsForList() = this.map(::replaceWordsWithDigitsForLine)

    private fun replaceWordsWithDigitsForLine(line: String) : String {
        var output = line
        replaceMap.forEach { (oldValue, newValue) -> output = output.replace(oldValue, newValue)}
        return output
    }

    private fun getSum(input: List<String>) = input.sumOf { getDigit(it) }.toString()

    private  fun getDigit(line: String): Int {
        val first = getFirstDigit(line)
        val last = getFirstDigit(line.reversed())
        return "$first$last".toInt()
    }

    private fun getFirstDigit(line: String): String {
        return line.first { it.isDigit() }.toString()
    }

    val replaceMap = mapOf(
        "one" to "one1one",
        "two" to "two2two",
        "three" to "three3three",
        "four" to "four4four",
        "five" to "five5five",
        "six" to "six6six",
        "seven" to "seven7seven",
        "eight" to "eight8eight",
        "nine" to "nine9nine",
    )

    const val exampleOne = "1abc2\n" +
            "pqr3stu8vwx\n" +
            "a1b2c3d4e5f\n" +
            "treb7uchet"

    const val exampleTwo = "two1nine\n" +
            "eightwothree\n" +
            "abcone2threexyz\n" +
            "xtwone3four\n" +
            "4nineeightseven2\n" +
            "zoneight234\n" +
            "7pqrstsixteen"
}
