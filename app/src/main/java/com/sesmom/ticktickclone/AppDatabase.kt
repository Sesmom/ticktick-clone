package com.sesmom.ticktickclone

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "nudge_database"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val dao = getInstance(context).taskDao()
                            dao.insert(Task(title = "Finalize Q3 roadmap deck", tag = "#work", time = "09:00", overdue = true))
                            dao.insert(Task(title = "Submit expense report", tag = "#finance", time = "Yesterday", overdue = true))
                            dao.insert(Task(title = "Morning review & standup notes", tag = "#work", time = "08:30"))
                            dao.insert(Task(title = "Design system audit - components", tag = "#design", time = "14:00"))
                            dao.insert(Task(title = "Read 30 pages - Deep Work", tag = "#learning", time = "21:00"))
                            dao.insert(Task(title = "Grocery run & meal prep", tag = "#personal", time = "18:00", done = true))
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
