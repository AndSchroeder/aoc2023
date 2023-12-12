package util

fun <T> T.debug() = also(::println)

fun <T> List<T>.debugList() = onEach(::println)