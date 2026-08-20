package com.sesmom.ticktickclone

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

val categoryPalette = listOf(
    0xFFEEE9FFL to 0xFF6D5BFFL,
    0xFFE0F5FFL to 0xFF0099CCL,
    0xFFFFE4F0L to 0xFFCC4D8CL,
    0xFFFFF3CCL to 0xFFB8860BL,
    0xFFD9FFEEL to 0xFF2ECC71L,
    0xFFFFE0E0L to 0xFFFF4D4DL,
    0xFFE0E7FFL to 0xFF4D6BFFL,
    0xFFFFEFD9L to 0xFFE08A00L
)

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).categoryDao()

    val categories: StateFlow<List<Category>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addCategory(name: String) {
        viewModelScope.launch {
            val tag = if (name.startsWith("#")) name else "#$name"
            val index = categories.value.size % categoryPalette.size
            val (bg, text) = categoryPalette[index]
            dao.insert(Category(name = tag, bgColorHex = bg, textColorHex = text))
        }
    }
}
