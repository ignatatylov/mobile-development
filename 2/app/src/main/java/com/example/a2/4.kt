package com.example.a2

data class Word(val value: String)
data class Context(val name: String)
data class Translate(val value: String)

typealias ContextMap = Map<Context, List<Translate>>

class Translator {
    private val dictionary: MutableMap<Word, MutableMap<Context, MutableList<Translate>>> = mutableMapOf()

    fun add(word: Word, context: Context, translate: Translate) {
        dictionary.computeIfAbsent(word) { mutableMapOf() }
            .computeIfAbsent(context) { mutableListOf() }
            .add(translate)
    }

    fun remove(word: Word, context: Context, translate: Translate) {
        dictionary[word]?.get(context)?.remove(translate)

        if (dictionary[word]?.get(context).isNullOrEmpty()) {
            dictionary[word]?.remove(context)
        }
        if (dictionary[word].isNullOrEmpty()) {
            dictionary.remove(word)
        }
    }

    fun getTranslate(word: Word): ContextMap {
        return dictionary[word] ?: emptyMap()
    }

    fun words(): Map<Word, ContextMap> {
        return dictionary
    }
}

fun main() {
    val translator = Translator()
    val scanner = java.util.Scanner(System.`in`)

    while (true) {
        val input = scanner.nextLine()
        val parts = input.split(" ", limit = 4)

        when (parts[0]) {
            "add" -> {
                if (parts.size < 4) {
                    println("Неверный формат команды add.")
                } else {
                    val word = Word(parts[1])
                    val context = Context(parts[2])
                    val translate = Translate(parts[3])
                    translator.add(word, context, translate)
                    println("Перевод добавлен.")
                }
            }

            "remove" -> {
                if (parts.size < 4) {
                    println("Неверный формат команды remove.")
                } else {
                    val word = Word(parts[1])
                    val context = Context(parts[2])
                    val translate = Translate(parts[3])
                    translator.remove(word, context, translate)
                    println("Перевод удалён.")
                }
            }

            "translate" -> {
                if (parts.size < 2) {
                    println("Неверный формат команды translate.")
                    return
                }

                val word = Word(parts[1])
                val translations = translator.getTranslate(word)
                if (translations.isEmpty()) {
                    println("Переводы для слова «${word.value}» не найдены.")
                } else {
                    println("Переводы слова «${word.value}»:")
                    for ((context, translates) in translations) {
                        println("В контексте «${context.name}»: ${translates.joinToString { it.value }}")
                    }
                }
            }

            "print" -> {
                val allWords = translator.words()
                if (allWords.isEmpty()) {
                    println("Словарь пуст.")
                } else {
                    for ((word, translations) in allWords) {
                        println("Переводы слова «${word.value}»:")
                        for ((context, translates) in translations) {
                            println("В контексте «${context.name}»: ${translates.joinToString { it.value }}")
                        }
                    }
                }
            }

            "exit" -> return

            else -> println("Неизвестная команда. Попробуйте снова.")
        }
    }
}
