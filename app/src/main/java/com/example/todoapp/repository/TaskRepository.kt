package com.example.todoapp.repository

import androidx.lifecycle.LiveData
import com.example.todoapp.data.dao.TaskDao
import com.example.todoapp.model.TaskModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {

    fun insertUpdateTask(taskModel: TaskModel) {
        GlobalScope.launch(Dispatchers.IO) {
            taskDao.updateInsertTask(taskModel)
        }
    }

    fun deleteTask(taskModel: TaskModel) {
        GlobalScope.launch(Dispatchers.IO) {
            taskDao.deleteTask(taskModel)
        }
    }

    fun getTasks(): LiveData<List<TaskModel>> {
        return taskDao.getTasks()
    }

    fun getCompletedTasks(): LiveData<List<TaskModel>> {
        return taskDao.getCompletedTasks()
    }
}