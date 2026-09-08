package com.sesmom.ticktickclone

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SubtaskDao {
    @Query("SELECT * FROM subtasks ORDER BY id ASC")
    fun getAll(): Flow<List<Subtask>>

    @Insert
    suspend fun insert(subtask: Subtask)

    @Update
    suspend fun update(subtask: Subtask)

    @Delete
    suspend fun delete(subtask: Subtask)
}
