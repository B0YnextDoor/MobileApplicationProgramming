package com.example.tabatatimer.android.utils

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import com.example.tabatatimer.android.models.WorkoutViewModel

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState> {
    error("No SnackbarHostState provided")
}

var clearAll: () -> Unit = {}
var setFont: (Float) -> Unit = {}
var setLocale: (String) -> Unit = {}

fun defineFunctions(viewModel: WorkoutViewModel) {
    clearAll = viewModel::clearAll
    setFont = viewModel::setSize
    setLocale = viewModel::setLocale
}

fun translate(locale: String, value: String) : String {
    if(locale == "en") return value
    return when(value) {
        "Workout List" -> "Список тренировок"
        "You have no workouts yet..." -> "Вы не добавили ни одной тренировки..."
        "Workout deleted" -> "Тренировка удалена"
        "Workout added" -> "Тренировка добавлена"
        "Add New Workout" -> "Добавление Тренировки"
        "Workout Name" -> "Название Тренировки"
        "Create Workout" -> "Создать Тренировку"
        "Edit Workout" -> "Изменение Тренировки"
        "Prepare Time (sec)" -> "Время подготовки (сек)"
        "Work Time (sec)" -> "Время работы (сек)"
        "Rest Time (sec)" -> "Время отдыха (сек)"
        "Cooldown Time (sec)" -> "Время перерыва (сек)"
        "Work/Rest Cycles" -> "Количество повторов"
        "Workout Repeats" -> "Повторы программы"
        "Rest Between Repeats (sec)" -> "Отдых между кругами (сек)"
        "Save Changes" -> "Сохранить Изменения"
        "Workout not found :(" -> "Тренировка не найдена"
        else -> value
    }
}