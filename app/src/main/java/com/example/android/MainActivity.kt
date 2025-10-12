package com.example.randomgenerator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomGeneratorAppTheme {
                RandomGeneratorApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomGeneratorApp() {
    var maxValue by remember { mutableStateOf("") }
    var listSize by remember { mutableStateOf("10") }
    var result by remember { mutableStateOf("") }
    var generatedNumbers by remember { mutableStateOf("") }
    var nullList by remember { mutableStateOf<List<Any?>>(emptyList()) }
    var nullResult by remember { mutableStateOf("") }
    var showResults by remember { mutableStateOf(false) }
    var showNullResults by remember { mutableStateOf(false) }

    val numberGenerator = NumberGenerator()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start
    ) {
        // Заголовок
        Text(
            text = "Генератор случайных чисел",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Секция 1: Поиск null элементов (ПОМЕСТИЛИ ПЕРВОЙ)
        Text(
            text = "Поиск индексов нулевых элементов",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Размер списка для генерации",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = listSize,
            onValueChange = { listSize = it },
            label = { Text("Например: 10") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    try {
                        val size = listSize.toInt()
                        if (size <= 0) {
                            nullResult = "Размер списка должен быть положительным числом"
                            showNullResults = true
                            return@Button
                        }
                        nullList = numberGenerator.createListWithNulls(size, 0.3)
                        nullResult = "Список сгенерирован. Нажмите 'Найти null элементы'"
                        showNullResults = true
                    } catch (e: NumberFormatException) {
                        nullResult = "Пожалуйста, введите корректное число"
                        showNullResults = true
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Сгенерировать список")
            }

            Button(
                onClick = {
                    if (nullList.isEmpty()) {
                        nullResult = "Сначала сгенерируйте список"
                        showNullResults = true
                        return@Button
                    }
                    val nullIndexes = numberGenerator.findNullIndexes(nullList)
                    nullResult = numberGenerator.formatNullResults(nullList, nullIndexes)
                    showNullResults = true
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Найти null")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Отображение результатов поиска null элементов
        if (showNullResults && nullResult.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Результаты поиска null элементов:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    val nullResultLines = nullResult.split("\n")
                    nullResultLines.forEach { line ->
                        Text(
                            text = line,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Разделитель между секциями
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            color = Color.Gray,
            thickness = 2.dp
        )

        // Секция 2: Генерация случайных чисел (оригинальный функционал)
        Text(
            text = "Генерация случайных чисел",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Введите верхнюю границу",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = maxValue,
            onValueChange = { maxValue = it },
            label = { Text("Например: 10000") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (maxValue.isBlank()) {
                    result = "Пожалуйста, введите верхнюю границу"
                    showResults = true
                    return@Button
                }

                try {
                    val max = maxValue.toInt()
                    if (max <= 0) {
                        result = "Введите положительное число"
                        showResults = true
                        return@Button
                    }

                    val numbers = numberGenerator.generateRandomNumbers(10, max)
                    val formattedResult = numberGenerator.formatResults(numbers)
                    result = formattedResult
                    generatedNumbers = numbers.joinToString(", ")
                    showResults = true

                } catch (e: NumberFormatException) {
                    result = "Пожалуйста, введите корректное число"
                    showResults = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Сгенерировать число", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Отображение результатов генерации чисел
        if (showResults && result.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E8))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Результаты генерации чисел:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    val resultLines = result.split("\n")
                    resultLines.forEach { line ->
                        Text(
                            text = line,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Сгенерированные данные: $generatedNumbers",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RandomGeneratorAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        content = content
    )
}