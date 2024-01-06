package com.example.jettodo2.data.repository

import com.example.jettodo2.domain.model.Task

interface TaskRepository {
    suspend fun create(title: String, description: String): Task
//    fun getAll(): Flow<List<Task>>
//    suspend fun delete(ta)
}
