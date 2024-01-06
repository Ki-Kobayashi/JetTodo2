package com.example.jettodo2.data.repository

import com.example.jettodo2.database.dao.TaskDao
import com.example.jettodo2.database.entiry.TaskEntity
import com.example.jettodo2.domain.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class LocalTaskRepository @Inject constructor(
    private val taskDao: TaskDao
) : TaskRepository {
    // TODO: Repository > 処理内で使用しやすいように、EntiryからModelに積め変える（Factoryクラスを使ってもOK）
    override suspend fun create(title: String, description: String): Task {
        val task = TaskEntity(
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

    override fun getAll(): Flow<List<Task>> {
        return taskDao.getAllTask().map { taskList ->
            taskList.map { task ->
                Task(
                    id = task.id,
                    title = task.title,
                    description = task.description,
                    isDone = task.done == 1,
                    updatedAt = Date(task.updatedAt),
                    createdAt = Date(task.createdAt),
                )
            }

        }
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
