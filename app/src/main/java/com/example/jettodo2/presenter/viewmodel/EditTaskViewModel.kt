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
    @Stable
    sealed interface UiState {
        /**
         * 初期表示時
         */
        data object Initial : UiState

        /**
         * 読み込み中時
         */
        data object Loading : UiState

        /**
         * 読み込み成功時
         */
        data class LoadSuccess(val task: Task) : UiState

        /**
         * 読み込み失敗時
         */
        // TODO:引数の【Throwable】：Exception（プログラムで回復可能）/Error（致命的）の親クラスで双方を受け取れるようにしている
        data class LoadError(val error: Throwable) : UiState

        /**
         * 入力受付時
         */
        data object Idle : UiState
    }

    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)

    private val taskId: Long = checkNotNull(savedStateHandle["taskId"])
    private val taskId2: Long =
        savedStateHandle.get<Long>("taskId") ?: throw IllegalStateException("id is required")


    // TODO: asStateFlow: MutableStateFlow　→　StateFlowに変換
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

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

    fun changeIdle() {
        _uiState.value = UiState.Idle
    }
}
