package com.sesmom.ticktickclone

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val emoji: String,
    val title: String,
    val streakDays: Int = 0,
    val checkedToday: Boolean = false
)
