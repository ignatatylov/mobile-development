package com.example.a2
import java.util.*
import kotlin.math.pow

fun sum(a: Int, b: Int): Int {
    return a + b
}

fun subtract(a: Int, b: Int): Int {
    return a - b
}

fun divide(a: Int, b: Int): Int {
    val answ = a / b
    return answ
}

fun multiply(a: Int, b: Int): Int {
    val answ = a * b
    return answ
}

fun pow(a: Int, b: Int): Int {
    val answ = a.toDouble().pow(b).toInt()
    return answ
}

fun max(numbers: List<Int>): Int {
    val max = numbers.maxOrNull() ?: 0
    return max
}

fun min(numbers: List<Int>): Int {
    val min = numbers.minOrNull() ?: 0
    return min
}

fun printList(numbers: List<Int>): String {
    return numbers.sorted().joinToString(" < ")
}

fun printAbout(name: String, age: Int): String {
    return "Привет, меня зовут $name, мне $age лет, через 5 лет мне будет ${age + 5} лет."
}

fun main() {
    val scanner = Scanner(System.`in`)
    while (true) {
        val input = scanner.nextLine()
        val parts = input.split(" ")

        when (parts[0]) {
            "sum" -> println(sum(parts[1].toInt(), parts[2].toInt()))
            "subtract" -> println(subtract(parts[1].toInt(), parts[2].toInt()))
            "divide" -> println(divide(parts[1].toInt(), parts[2].toInt()))
            "multiply" -> println(multiply(parts[1].toInt(), parts[2].toInt()))
            "pow" -> println(pow(parts[1].toInt(), parts[2].toInt()))
            "max" -> println(max(parts.drop(1).map { it.toInt() }))
            "min" -> println(min(parts.drop(1).map { it.toInt() }))
            "print_list" -> println(printList(parts.drop(1).map { it.toInt() }))
            "print_about" -> println(printAbout(parts[1], parts[2].toInt()))
            "exit" -> return
            else -> println("Команда не распознана.")
        }
    }
}