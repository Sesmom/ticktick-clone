package com.sesmom.ticktickclone

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).taskDao()
    private val subtaskDao = AppDatabase.getInstance(application).subtaskDao()

    val tasks: StateFlow<List<Task>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val subtasks: StateFlow<List<Subtask>> = subtaskDao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleDone(task: Task) {
        viewModelScope.launch {
            dao.update(task.copy(done = !task.done))
        }
    }

    fun addTask(title: String, tag: String, time: String, desc: String = "", quadrant: Int = 0) {
        viewModelScope.launch {
            dao.insert(Task(title = title, tag = tag, time = time, desc = desc, quadrant = quadrant))
        }
    }

    fun toggleDoneById(id: Int) {
        viewModelScope.launch {
            val task = tasks.value.find { it.id == id } ?: return@launch
            dao.update(task.copy(done = !task.done))
        }
    }

    fun addSubtask(taskId: Int, title: String) {
        viewModelScope.launch {
            subtaskDao.insert(Subtask(taskId = taskId, title = title))
        }
    }

    fun toggleSubtask(id: Int) {
        viewModelScope.launch {
            val sub = subtasks.value.find { it.id == id } ?: return@launch
            subtaskDao.update(sub.copy(done = !sub.done))
        }
    }
}
