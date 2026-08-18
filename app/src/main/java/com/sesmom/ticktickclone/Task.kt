package com.sesmom.ticktickclone

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val tag: String = "",
    val time: String = "",
    val done: Boolean = false,
    val overdue: Boolean = false,
    val quadrant: Int = 0
)
