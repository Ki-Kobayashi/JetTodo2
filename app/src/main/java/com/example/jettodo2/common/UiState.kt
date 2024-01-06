package com.example.jettodo2.common

sealed interface UiState {
    data object Idle : UiState
    data object Success : UiState
    data class InputErr(val errMessage: String) : UiState
    data class CreateError(val e: Exception) : UiState
}
