package day


object Day01 : Day("01", "142", "281") {
    override fun examplePartOne() = getSum(exampleOne.split("\n"))
    override fun examplePartTwo() = getSum(exampleTwo.split("\n").replaceWordsWithDigitsForList())
    override fun solvePartOne() = getSum(getInputList())
    override fun solvePartTwo() = getSum(getInputList().replaceWordsWithDigitsForList())

    private fun List<String>.replaceWordsWithDigitsForList() = this.map(::replaceWordsWithDigitsForLine)

    private fun replaceWordsWithDigitsForLine(line: String) : String {
        var output = line
        replaceMap .forEach { (oldValue, newValue) -> output = output.replace(oldValue, newValue)}
        return output
    }

    private fun getSum(input: List<String>) = input.sumOf { getDigit(it) }.toString()

    private  fun getDigit(line: String): Int {
        val first = getFirstDigit(line)
        val last = getFirstDigit(line.reversed())
        return "$first$last".toInt()
    }

    private fun getFirstDigit(line: String) = line.first { it.isDigit() }.toString()

    private val replaceMap = mapOf(
        "one" to "o1e",
        "two" to "t2",
        "three" to "t3e",
        "four" to "4",
        "five" to "5e",
        "six" to "6",
        "seven" to "7n",
        "eight" to "e8",
        "nine" to "n9e",
    )

    private const val exampleOne = "1abc2\n" +
            "pqr3stu8vwx\n" +
            "a1b2c3d4e5f\n" +
            "treb7uchet"

    private const val exampleTwo = "two1nine\n" +
            "eightwothree\n" +
            "abcone2threexyz\n" +
            "xtwone3four\n" +
            "4nineeightseven2\n" +
            "zoneight234\n" +
            "7pqrstsixteen"
}
