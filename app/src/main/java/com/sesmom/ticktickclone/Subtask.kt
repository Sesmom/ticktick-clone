package com.sesmom.ticktickclone

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subtasks")
data class Subtask(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val taskId: Int,
    val title: String,
    val done: Boolean = false
)
