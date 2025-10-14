package com.example.randomgenerator

import java.util.Random

class NullElementsProcessor {

    /**
     * Находит индексы всех нулевых элементов в списке
     */
    fun findNullIndexes(list: List<Any?>): IntArray {
        val nullIndexes = mutableListOf<Int>()

        for ((index, element) in list.withIndex()) {
            if (element == null) {
                nullIndexes.add(index)
            }
        }

        return nullIndexes.toIntArray()
    }

    /**
     * Создает тестовый список со случайными null элементами
     */
    fun createTestList(size: Int, nullProbability: Double = 0.3): List<Any?> {
        val random = Random()
        val list = mutableListOf<Any?>()

        for (i in 0 until size) {
            list.add(
                if (random.nextDouble() < nullProbability) {
                    null
                } else {
                    "Элемент-${i + 1}"
                }
            )
        }

        return list
    }

    /**
     * Форматирует список для красивого отображения
     */
    fun formatListForDisplay(list: List<Any?>): String {
        return list.joinToString(", ") { element ->
            if (element == null) "NULL" else "'$element'"
        }
    }

    /**
     * Форматирует индексы для отображения
     */
    fun formatIndexesForDisplay(indexes: IntArray): String {
        return if (indexes.isEmpty()) {
            "Нет нулевых элементов"
        } else {
            indexes.joinToString(", ", "[", "]")
        }
    }
}