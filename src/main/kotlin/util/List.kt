package util

fun List<Long>.sum(): Long = this.reduce { acc, number -> acc + number }
fun List<Long>.product(): Long = this.reduce { acc, number -> acc * number }
fun List<Long>.lcm() = reduce { acc, number -> lcm(acc, number) }
fun String.toStringList() = toList().map(Char::toString)
fun String.toLongList() = split("""\s""".toRegex()).map(String::toLong)
fun List<String>.toIntList() = map(String::toInt)

fun <T>List<T>.combinations() = this.mapIndexed { y, yValue ->
    this.mapIndexed { x, xValue ->
        if (x <= y) return@mapIndexed null
        xValue to yValue
    }.filterNotNull()
}.flatten()
