package com.example.jettodo2.data.repository

import com.example.jettodo2.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun create(title: String, description: String): Task
    fun getAll(): Flow<List<Task>>
    suspend fun getTaskById(id: Long): Task?
    suspend fun updateTask(updatedTask: Task)
    suspend fun deleteTask(deleteTask: Task)

//    suspend fun delete(ta)
}
