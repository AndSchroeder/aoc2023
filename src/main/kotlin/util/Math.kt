package util

import kotlin.math.abs

fun gcd(firstNumber: Long, secondNumber: Long): Long {
    return if (secondNumber == 0L) firstNumber else gcd(secondNumber, firstNumber % secondNumber)
}

fun lcm(firstNumber: Long, secondNumber: Long): Long {
    return if (firstNumber == 0L || secondNumber == 0L) {
        0
    } else {
        (abs(firstNumber) / gcd(firstNumber, secondNumber)) * abs(secondNumber)
    }
}

