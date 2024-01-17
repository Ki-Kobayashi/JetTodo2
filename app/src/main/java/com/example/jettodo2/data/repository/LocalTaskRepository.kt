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
            taskList.map { taskEntity ->
                taskEntity.toModel()
            }

        }
    }

    override suspend fun getTaskById(id: Long): Task? {
        return taskDao.getTaskById(id)?.toModel()
    }

    override suspend fun updateTask(updatedTask: Task) {
//        val entity = TaskEntity(
//            id = updatedTask.id,
//            title = updatedTask.title,
//            description = updatedTask.description,
//            done = if (updatedTask.isDone) DONE else NOT_DONE,
//            updatedAt = System.currentTimeMillis(),
//            createdAt = updatedTask.createdAt.time,
//        )
        val entity = updatedTask.toEntity()
        taskDao.updateTask(entity)
    }

    override suspend fun deleteTask(deleteTask: Task) {
        val entity = deleteTask.toEntity()
        taskDao.deleteTask(entity)
    }

    // TODO: 【拡張関数】以下のように書くことで、taskEntiry.toModel()と呼ぶことで、
    //  　　　　Task型として取得できるようになる
    //  　　　　　※：自身（例：taskEntity）を表すときは、関数内で「this」を使用する。
    private fun TaskEntity.toModel(): Task {
        return Task(
            id = id,
            title = title,
            description = description,
            isDone = done == 1,
            createdAt = Date(createdAt),
            updatedAt = Date(updatedAt),
        )
    }

    private fun Task.toEntity(): TaskEntity {
        return TaskEntity(
            id = id,
            title = title,
            description = description,
            done = if (isDone) DONE else NOT_DONE,
            createdAt = createdAt.time,
            updatedAt = updatedAt.time,
        )
    }

    companion object {
        private const val DONE: Int = 1
        private const val NOT_DONE: Int = 0
    }
}
