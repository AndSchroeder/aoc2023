package util

fun <T> Sequence<T>.repeat(): Sequence<T> {
    return sequence {
        while (true) {
            yieldAll(this@repeat)
        }
    }
}

fun <T> Sequence<T>.intersection(other: Sequence<T>) = filter { firstValue -> other.contains(firstValue) }
