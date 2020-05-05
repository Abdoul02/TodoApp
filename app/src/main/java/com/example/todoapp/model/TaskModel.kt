package com.example.todoapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

const val TASK_TABLE = "task_table"

@Entity(tableName = TASK_TABLE)
data class TaskModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String,
    val description: String,
    val status: Int
)