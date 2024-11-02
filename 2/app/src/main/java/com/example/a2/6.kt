package com.example.a2

import java.util.*

data class Student(
    val id: Int,
    var name: String,
    var age: Int,
    var points: Int
)

class StudentManager {
    private val students = mutableListOf<Student>()
    private var nextId = 1

    fun addStudent(studentInfo: String) {
        val parts = studentInfo.split(" ")
        if (parts.size != 3) {
            println("Ошибка: неверный формат ввода студента")
            return
        }
        val name = parts[0]
        val age = parts[1].toInt();
        val points = parts[2].toInt();
        val student = Student(nextId++, name, age, points)
        students.add(student)
    }

    fun addStudentList(studentList: String) {
        val studentEntries = studentList.split(",")
        studentEntries.forEach {
            addStudent(it.trim())
        }
    }

    fun removeStudent(id: Int) {
        val student = students.find { it.id == id }
        if (student != null) {
            students.remove(student)
            println("Удален студент с id $id")
        } else {
            println("Студент с id $id не найден")
        }
    }

    fun updatePoints(id: Int, newPoints: Int) {
        val student = students.find { it.id == id }
        if (student != null) {
            student.points = newPoints
            println("Баллы студента с id $id обновлены до $newPoints")
        } else {
            println("Студент с id $id не найден")
        }
    }

    fun renameStudent(id: Int, newName: String) {
        val student = students.find { it.id == id }
        if (student != null) {
            student.name = newName
            println("Имя студента с id $id изменено на $newName")
        } else {
            println("Студент с id $id не найден")
        }
    }

    fun printSortByPoints() {
        students.sortedByDescending { it.points }.forEach {
            println("${it.id} ${it.name} (${it.age} лет) - ${it.points} балла")
        }
    }

    fun printSortByNames() {
        students.sortedBy { it.name }.forEach {
            println("${it.id} ${it.name} (${it.age} лет) - ${it.points} балла")
        }
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    val manager = StudentManager()

    val initialInput = scanner.nextLine()
    manager.addStudentList(initialInput)

    while (true) {
        val command = scanner.nextLine().trim()
        when {
            command.startsWith("add") -> {
                val studentInfo = command.removePrefix("add").trim()
                manager.addStudent(studentInfo)
            }
            command.startsWith("remove") -> {
                val id = command.removePrefix("remove").trim().toIntOrNull()
                if (id != null) {
                    manager.removeStudent(id)
                } else {
                    println("Ошибка: неверный id")
                }
            }
            command.startsWith("update_points") -> {
                val parts = command.removePrefix("update_points").trim().split(" ")
                if (parts.size == 2) {
                    val id = parts[0].toInt()
                    val newPoints = parts[1].toInt()
                    manager.updatePoints(id, newPoints)
                } else {
                    println("Ошибка: неверное количество параметров")
                }
            }
            command.startsWith("rename") -> {
                val parts = command.removePrefix("rename").trim().split(" ")
                if (parts.size == 2) {
                    val id = parts[0].toInt()
                    val newName = parts[1]
                    manager.renameStudent(id, newName)
                } else {
                    println("Ошибка: неверное количество параметров")
                }
            }
            command == "print_sort_by_points" -> manager.printSortByPoints()
            command == "print_sort_by_names" -> manager.printSortByNames()
            command == "exit" -> break
            else -> println("Неизвестная команда")
        }
    }
}
