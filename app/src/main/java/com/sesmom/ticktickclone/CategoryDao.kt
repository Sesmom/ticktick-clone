package com.sesmom.ticktickclone

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY id ASC")
    fun getAll(): Flow<List<Category>>

    @Insert
    suspend fun insert(category: Category)
}
