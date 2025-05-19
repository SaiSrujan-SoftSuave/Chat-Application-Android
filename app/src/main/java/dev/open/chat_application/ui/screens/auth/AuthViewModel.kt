package dev.open.chat_application.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.open.chat_application.core.SessionManager
import dev.open.chat_application.core.UiEvent
import dev.open.chat_application.data.repository.AuthRepository
import dev.open.chat_application.data.network.model.ResultWrapper
import dev.open.chat_application.data.network.model.SignInRequest
import dev.open.chat_application.data.network.model.SignInResponse
import dev.open.chat_application.data.network.model.SignUpRequest
import dev.open.chat_application.data.network.model.SignUpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _signInUiEvent = MutableStateFlow<UiEvent<SignInResponse>>(UiEvent.Empty)
    val signInUiEvent: StateFlow<UiEvent<SignInResponse>> = _signInUiEvent.asStateFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val request = SignInRequest(email = email.trim(), password = password.trim())
            withContext(Dispatchers.IO) {
                authRepository.signIn(request).collect { result ->
                    when (result) {
                        is ResultWrapper.Loading -> {
                            _signInUiEvent.update {
                                UiEvent.Loading
                            }
                        }

                        is ResultWrapper.Success -> {
                            val networkDate = result.data
                            _signInUiEvent.update {
                                UiEvent.Success(result.data.data!!)
                            }
                            networkDate.data?.accessToken?.let { sessionManager.upsertSession(it) }
                            networkDate.data?.userId.let { sessionManager.upsertUserId(it!!) }
                        }

                        is ResultWrapper.Error -> {
                            _signInUiEvent.update {
                                UiEvent.Failure(result.message!!)
                            }
                        }
                    }
                }
            }
        }
    }

    private val _signUpUiEvent = MutableStateFlow<UiEvent<SignUpResponse>>(UiEvent.Empty)
    val signUpUiEvent: StateFlow<UiEvent<SignUpResponse>> = _signUpUiEvent.asStateFlow()

    fun signUp(email: String, password: String, name: String) {
        viewModelScope.launch {
            val request = SignUpRequest(name = name, email = email.trim(), password =  password.trim())
            withContext(Dispatchers.IO) {
                authRepository.signUp(request).collect { result ->
                    when (result) {
                        is ResultWrapper.Loading -> {
                            _signUpUiEvent.update {
                                UiEvent.Loading
                            }
                        }

                        is ResultWrapper.Success -> {
                            _signUpUiEvent.update {
                                UiEvent.Success(result.data.data!!)
                            }
                        }

                        is ResultWrapper.Error -> {
                            _signUpUiEvent.update {
                                UiEvent.Failure(result.message!!)
                            }
                        }
                    }
                }
            }
        }
    }
}

