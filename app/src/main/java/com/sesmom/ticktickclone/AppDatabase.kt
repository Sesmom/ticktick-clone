package com.sesmom.ticktickclone

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Task::class, Category::class, Habit::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao
    abstract fun habitDao(): HabitDao

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
                            dao.insert(Task(title = "Finalize Q3 roadmap deck", tag = "#work", time = "09:00", overdue = true, desc = "Review milestones with the team and lock scope before Friday", quadrant = 0))
                            dao.insert(Task(title = "Submit expense report", tag = "#finance", time = "Yesterday", overdue = true, desc = "Attach receipts from the conference trip and submit for approval", quadrant = 1))
                            dao.insert(Task(title = "Morning review & standup notes", tag = "#work", time = "08:30", desc = "Go over yesterday's blockers and today's priorities with the team", quadrant = 0))
                            dao.insert(Task(title = "Design system audit - components", tag = "#design", time = "14:00", desc = "Check button, input, and card components for consistency", quadrant = 1))
                            dao.insert(Task(title = "Read 30 pages - Deep Work", tag = "#learning", time = "21:00", desc = "Continue the chapter on focus and deep work habits", quadrant = 3))
                            dao.insert(Task(title = "Grocery run & meal prep", tag = "#personal", time = "18:00", done = true, desc = "Pick up ingredients for the week and prep Sunday dinner", quadrant = 2))

                            val catDao = getInstance(context).categoryDao()
                            catDao.insert(Category(name = "#work", bgColorHex = 0xFFEEE9FFL, textColorHex = 0xFF6D5BFFL))
                            catDao.insert(Category(name = "#finance", bgColorHex = 0xFFE0F5FFL, textColorHex = 0xFF0099CCL))
                            catDao.insert(Category(name = "#design", bgColorHex = 0xFFFFE4F0L, textColorHex = 0xFFCC4D8CL))
                            catDao.insert(Category(name = "#learning", bgColorHex = 0xFFFFF3CCL, textColorHex = 0xFFB8860BL))
                            catDao.insert(Category(name = "#personal", bgColorHex = 0xFFD9FFEEL, textColorHex = 0xFF2ECC71L))

                            val habitDao = getInstance(context).habitDao()
                            habitDao.insert(Habit(emoji = "📚", title = "Read 30 pages", streakDays = 8, checkedToday = true))
                            habitDao.insert(Habit(emoji = "🧘", title = "Meditate", streakDays = 3, checkedToday = false))
                            habitDao.insert(Habit(emoji = "💧", title = "Drink 2L water", streakDays = 21, checkedToday = true))
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
