package com.example.jettodo2.presenter.viewmodel

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jettodo2.data.repository.TaskRepository
import com.example.jettodo2.domain.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {


    private val taskId: Long = checkNotNull(savedStateHandle["taskId"])
    private val taskId2: Long =
        savedStateHandle.get<Long>("taskId") ?: throw IllegalStateException("id is required")

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)
    val uiState: StateFlow<UiState> =
        _uiState.asStateFlow() // TODO: asStateFlow: MutableStateFlow　→　StateFlowに変換

    /**
     * 初期表示データの読み込み
     */
    fun loadData() {
        Log.d("EditTaskViewModel", "@@@@@ taskId : $taskId")
        Log.d("EditTaskViewModel", "@@@@@ id : $taskId2")

        _uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val task = taskRepository.getTaskById(taskId)
                if (task == null) {
                    _uiState.value = UiState.LoadError(IllegalArgumentException("id is not found"))
                    return@launch
                }
                _uiState.value = UiState.LoadSuccess(task)
            } catch (e: Exception) {
                _uiState.value = UiState.LoadError(e)
            }
        }
    }

    /**
     * 入力受付状態へ変更
     */
    fun changeIdle() {
        val currentState = _uiState.value
        if (currentState is UiState.LoadSuccess) {
            _uiState.value = UiState.Idle(currentState.task)
        } else if (currentState is UiState.InputError) {
            _uiState.value = UiState.Idle(currentState.task)
        } else if (currentState is UiState.UpdateError) {
            _uiState.value = UiState.Idle(currentState.task)
        } else if (currentState is UiState.ConfirmDelete) {
            _uiState.value = UiState.Idle(currentState.task)
        } else if (currentState is UiState.DeleteError) {
            _uiState.value = UiState.Idle(currentState.task)
        }
    }

    /**
     * タスクの更新
     */
    fun updateTask(title: String, description: String) {
        // TODO: 【多重タップ防止】現在の UIState が Idle の時だけ、更新するようにする
        //  　　　（更新中は UpdateInProgress のため、多重に実行されずに済む）
        val currentState = _uiState.value
        if (currentState !is UiState.Idle) {
            return
        }
        _uiState.value = UiState.UpdateInProgress
        // 入力チェック
        if (title.trim().isEmpty()) {
            _uiState.value =
                UiState.InputError(task = currentState.task, errMessage = "タイトルを入力してください。")
            return
        }
        if (description.trim().isEmpty()) {
            _uiState.value =
                UiState.InputError(task = currentState.task, errMessage = "詳細を入力してください。")
            return
        }

        viewModelScope.launch {
            try {
                taskRepository.updateTask(
                    currentState.task.copy(
                        title = title,
                        description = description,
                    )
                )
                _uiState.value = UiState.UpdateSuccess
            } catch (e: Exception) {
                _uiState.value = UiState.UpdateError(currentState.task, e)
            }
        }
    }

    fun showDeleteDialog() {
        val currentState = _uiState.value
        if (currentState !is UiState.Idle) {
            return
        }
        _uiState.value = UiState.ConfirmDelete(currentState.task)
    }

    fun deleteTask() {
        val currentState = _uiState.value
        // TODO: 【多重タップ防止】現在の UIState が ConfirmDelete の時だけ、更新するようにする
        //  　　　（更新中は DeleteInProgress のため、多重に実行されずに済む）
        // Task削除:　削除ダイアログからの呼び出し以外は、何もしない（削除ダイアログ状態のときのみ、削除できるようにする）
        if (currentState !is UiState.ConfirmDelete) {
            Log.d("AAAAAA", "uiState: ${uiState.value}")
            return
        }
        _uiState.value = UiState.DeleteInProgress
        viewModelScope.launch {
            try {
                taskRepository.deleteTask(currentState.task)
                _uiState.value = UiState.DeleteSuccess
            } catch (e: Exception) {
                _uiState.value = UiState.DeleteError(task = currentState.task, err = e)
            }
        }
    }

    /**
     * タスク編集画面：UI状態
     */
    @Stable
    sealed interface UiState {
        // TODO: changeIdleに変更するものは、引数にtaskをもつようにしておく
        data object Initial : UiState // 初期表示
        data object Loading : UiState // 読み込み中
        data class LoadSuccess(val task: Task) : UiState // 読み込み成功
        data class Idle(val task: Task) : UiState // 入力受付中

        // TODO:引数の【Throwable】：Exception（プログラムで回復可能）/Error（致命的）の親クラスで双方を受け取れるようにしている
        data class LoadError(val error: Throwable) : UiState // 読み込みエラー
        data class InputError(val task: Task, val errMessage: String) : UiState // ユーザー入力値エラー
        data object UpdateInProgress : UiState // 更新処理中
        data object UpdateSuccess : UiState // 更新成功
        data class UpdateError(val task: Task, val err: Exception) : UiState // 更新エラー
        data class ConfirmDelete(val task: Task) : UiState
        data object DeleteInProgress : UiState
        data object DeleteSuccess : UiState
        data class DeleteError(val task: Task, val err: Exception) : UiState

    }
}
