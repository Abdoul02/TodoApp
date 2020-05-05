package com.example.todoapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todoapp.model.TASK_TABLE
import com.example.todoapp.model.TaskModel

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateInsertTask(taskModel: TaskModel)

    @Delete
    suspend fun deleteTask(taskModel: TaskModel)

    @Query("SELECT * FROM $TASK_TABLE ORDER BY id")
    fun getTasks(): LiveData<List<TaskModel>>

    //1 -> completed, 0-> in progress
    @Query("SELECT * FROM $TASK_TABLE WHERE status = 1 ORDER BY id")
    fun getCompletedTasks(): LiveData<List<TaskModel>>
}