package com.example.todoapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.todoapp.model.TaskModel
import com.example.todoapp.repository.TaskRepository
import javax.inject.Inject

class MainViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {

    private val taskList: LiveData<List<TaskModel>> = taskRepository.getTasks()
    private val completedTaskList: LiveData<List<TaskModel>> = taskRepository.getCompletedTasks()

    val completedTask: LiveData<List<TaskModel>>
        get() = completedTaskList
    val tasks: LiveData<List<TaskModel>>
        get() = taskList

    fun insertUpdateTask(taskModel: TaskModel) {
        taskRepository.insertUpdateTask(taskModel)
    }

    fun deleteTask(taskModel: TaskModel) {
        taskRepository.deleteTask(taskModel)
    }

    fun getPercentage(totalTask: Int, taskCompleted: Int): Double {
        return (taskCompleted.div(totalTask.toDouble())) * 100
    }

}
