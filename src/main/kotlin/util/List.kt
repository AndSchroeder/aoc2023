package util

fun List<Long>.sum(): Long = this.reduce {acc, number -> acc + number }
fun List<Long>.product(): Long = this.reduce {acc, number -> acc * number }
fun List<Long>.lcm() = reduce {acc, number -> lcm(acc, number) }
fun String.toStringList() = toList().map(Char::toString)
