package com.example.randomgenerator

import java.util.Random

class NumberGenerator {

    fun generateRandomNumbers(count: Int, maxValue: Int): IntArray {
        val random = Random()
        val numbers = IntArray(count)

        for (i in 0 until count) {
            numbers[i] = random.nextInt(maxValue) + 1
        }

        return numbers
    }

    fun calculateSum(numbers: IntArray): Int {
        return numbers.sum()
    }

    fun findMax(numbers: IntArray): Int {
        return numbers.maxOrNull() ?: 0
    }

    fun findMin(numbers: IntArray): Int {
        return numbers.minOrNull() ?: 0
    }

    fun calculateAverage(numbers: IntArray): Double {
        return if (numbers.isNotEmpty()) {
            numbers.average()
        } else {
            0.0
        }
    }

    fun formatResults(numbers: IntArray): String {
        return """
            Сгенерированные числа: ${numbers.joinToString(", ")}
            Сумма: ${calculateSum(numbers)}
            Максимум: ${findMax(numbers)}
            Минимум: ${findMin(numbers)}
            Среднее: ${"%.2f".format(calculateAverage(numbers))}
        """.trimIndent()
    }

    // Новые методы для работы с нулевыми элементами

    /**
     * Создает список с нулевыми элементами
     * @param size размер списка
     * @param nullProbability вероятность появления null (от 0.0 до 1.0)
     * @return список с случайными null элементами
     */
    fun createListWithNulls(size: Int, nullProbability: Double = 0.3): List<Any?> {
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
     * Находит индексы всех нулевых элементов в списке
     * @param list список элементов (может содержать null)
     * @return массив индексов нулевых элементов
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
     * Форматирует результат поиска null элементов для отображения
     * @param list исходный список
     * @param nullIndexes массив индексов null элементов
     * @return отформатированная строка с результатами
     */
    fun formatNullResults(list: List<Any?>, nullIndexes: IntArray): String {
        return """
            Исходный список: ${list.joinToString(", ") { it?.toString() ?: "NULL" }}
            Индексы нулевых элементов: ${if (nullIndexes.isEmpty()) "нет" else nullIndexes.joinToString(", ")}
            Количество нулевых элементов: ${nullIndexes.size}
        """.trimIndent()
    }
}