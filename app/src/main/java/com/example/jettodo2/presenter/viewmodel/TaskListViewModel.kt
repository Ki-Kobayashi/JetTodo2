package com.example.jettodo2.presenter.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jettodo2.data.repository.TaskRepository
import com.example.jettodo2.database.entiry.TaskEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val repository: TaskRepository,
) : ViewModel() {
    val taskList = repository.getAll()

    // 新規TODO追加：タイトル入力値の管理
//    private var _title by mutableStateOf("")
//    val title by State<>
    var title by mutableStateOf("")

    // 新規TODO追加：詳細入力値の管理
    var description by mutableStateOf("")

    // タスク追加ダイアログが開いているか
    var isShowAddTaskDialog by mutableStateOf(false)

    // 削除確認ダイアログが開いているか
    var isShowComfirmDialog by mutableStateOf(false)

    // タスク一覧の監視（TODO: distinctUntilChanged(): 値に変更がない場合は更新しない設定）
//    val taskList = repository.distinctUntilChanged()

    fun createTask(title: String, description: String) {
        // TODO: Flow/suspendは、コルーチン内で呼び出す
        viewModelScope.launch {
            val newTask = TaskEntity(
                title = title,
                description = description,
                done = 0,
                updatedAt = System.currentTimeMillis(),
                createdAt = System.currentTimeMillis(),
            )
            val task = repository.create(title = title, description = description)
//            taskDao.insertTask(newTask)
            Log.d(TaskListViewModel::class.simpleName, "created task: $task")
        }
    }

//    fun deleteTask(task: TaskEntiry) {
//        viewModelScope.launch {
//            taskDao.deleteTask(task)
//        }
//    }
}
