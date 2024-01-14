package com.example.jettodo2.presenter.viewmodel

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jettodo2.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
//    private val stringResourcesProvider: StringResourcesProvider,
    private val taskRepository: TaskRepository,
) : ViewModel() {
    //TODO: 非同期に（コルーチン内での処理で）状態が変わる（断続的に値が代わり送出する）ため、MutableStateFlowを使用する
    private var _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun create(title: String, description: String) {
        // 入力チェック
        if (title.trim().isEmpty()) {
            _uiState.value =
//                UiState.InputErr(errMessage = stringResource(id = R.string.input_err_title))
                UiState.InputErr(
//                    errMessage = stringResourcesProvider.getString(
//                        R.string.input_err_title
//                    )
                    errMessage = "タイトルを入力してください"
                )
            return
        }
        if (description.trim().isEmpty()) {
            _uiState.value =
                UiState.InputErr(
//                    errMessage = stringResourcesProvider.getString(
//                        R.string.input_err_description
//                    )
                    errMessage = "詳細を入力してください"
                )
            return
        }
        try {
            // TODO: Flow/suspendは、コルーチン内で呼び出す
            viewModelScope.launch {
                taskRepository.create(title, description)
            }
            _uiState.value = UiState.Success
        } catch (e: Exception) {
            _uiState.value = UiState.CreateError(e)
        }
    }

    fun changeIdle() {
        _uiState.value = UiState.Idle
    }

    // TODO: ViewのUiStateは、ViewModelにまとめておく
    @Stable
    sealed interface UiState {
        data object Idle : UiState
        data object Success : UiState
        data class InputErr(val errMessage: String) : UiState
        data class CreateError(val e: Exception) : UiState
    }
}
