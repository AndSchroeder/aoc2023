package util

fun <T> Sequence<T>.repeat(): Sequence<T> {
    return sequence {
        while (true) {
            yieldAll(this@repeat)
        }
    }
}
