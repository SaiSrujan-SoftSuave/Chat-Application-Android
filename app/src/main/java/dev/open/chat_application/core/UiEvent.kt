package dev.open.chat_application.core

sealed class UiEvent<out T> {
    data object Loading : UiEvent<Nothing>()
    data class Success<T>(val data: T) : UiEvent<T>()
    data class Failure(val error: String) : UiEvent<Nothing>()
    data object Empty : UiEvent<Nothing>()
}
