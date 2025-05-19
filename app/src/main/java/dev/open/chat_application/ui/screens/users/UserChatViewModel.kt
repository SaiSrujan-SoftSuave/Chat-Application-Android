package dev.open.chat_application.ui.screens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.open.chat_application.core.SessionManager
import dev.open.chat_application.core.UiEvent
import dev.open.chat_application.data.network.model.BaseResponse
import dev.open.chat_application.data.network.model.ResultWrapper
import dev.open.chat_application.data.network.model.UserData
import dev.open.chat_application.data.network.model.UserModel
import dev.open.chat_application.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserChatViewModel(
    private val chatRepository: ChatRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _userUiState = MutableStateFlow<UiEvent<List<UserModel>>>(UiEvent.Empty)
    val userState: StateFlow<UiEvent<List<UserModel>>> = _userUiState.asStateFlow()

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        viewModelScope.launch {
            chatRepository.getAllUsers().collect { result ->
                when (result) {
                    is ResultWrapper.Error -> {
                        _userUiState.value = UiEvent.Failure(result.message!!)
                    }

                    is ResultWrapper.Loading -> {
                        _userUiState.value = UiEvent.Loading
                    }

                    is ResultWrapper.Success<BaseResponse<UserData>> -> {
                        print(result.data)
                        val users: List<UserModel>? = result.data.data.users?.filter { user ->
                            user.id != sessionManager.userId.first()
                        }
                        _userUiState.value = UiEvent.Success(users ?: emptyList())
                    }
                }
            }
        }
    }
}