package com.sesmom.ticktickclone

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Task::class], version = 2, exportSchema = false)
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
                ).fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val dao = getInstance(context).taskDao()
                            dao.insert(Task(title = "Finalize Q3 roadmap deck", tag = "#work", time = "09:00", overdue = true, desc = "Review milestones with the team and lock scope before Friday"))
                            dao.insert(Task(title = "Submit expense report", tag = "#finance", time = "Yesterday", overdue = true, desc = "Attach receipts from the conference trip and submit for approval"))
                            dao.insert(Task(title = "Morning review & standup notes", tag = "#work", time = "08:30", desc = "Go over yesterday's blockers and today's priorities with the team"))
                            dao.insert(Task(title = "Design system audit - components", tag = "#design", time = "14:00", desc = "Check button, input, and card components for consistency"))
                            dao.insert(Task(title = "Read 30 pages - Deep Work", tag = "#learning", time = "21:00", desc = "Continue the chapter on focus and deep work habits"))
                            dao.insert(Task(title = "Grocery run & meal prep", tag = "#personal", time = "18:00", done = true, desc = "Pick up ingredients for the week and prep Sunday dinner"))
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
