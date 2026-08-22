package com.sesmom.ticktickclone

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).habitDao()

    val habits: StateFlow<List<Habit>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleCheckIn(id: Int) {
        viewModelScope.launch {
            val habit = habits.value.find { it.id == id } ?: return@launch
            val nowChecked = !habit.checkedToday
            val newStreak = if (nowChecked) habit.streakDays + 1 else (habit.streakDays - 1).coerceAtLeast(0)
            dao.update(habit.copy(checkedToday = nowChecked, streakDays = newStreak))
        }
    }

    fun addHabit(emoji: String, title: String) {
        viewModelScope.launch {
            dao.insert(Habit(emoji = emoji, title = title))
        }
    }
}
