package com.example.jettodo2.data.repository

import com.example.jettodo2.database.dao.TaskDao
import com.example.jettodo2.database.entiry.TaskEntiry
import com.example.jettodo2.domain.model.Task
import java.util.Date
import javax.inject.Inject

class LocalTaskRepostory @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    override suspend fun create(title: String, description: String): Task {
        val task = TaskEntiry(
            id = 0,
            title = title,
            description = description,
            done = 0,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
        )
        val id = taskDao.insertTask(task)
        return Task(
            id = id,
            title = title,
            description = description,
            isDone = false,
            createdAt = Date(task.createdAt),
            updatedAt = Date(task.updatedAt),
        )
    }

//    override fun getAll(): Flow<List<Task>> {
//        val taskList = taskDao.getAllTask()
//        taskList.map { task ->
//            Task(
//                 id = task.id
//            )
//        }
//    }
}
