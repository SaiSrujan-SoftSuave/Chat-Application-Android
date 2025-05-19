package dev.open.chat_application.data.repository

import dev.open.chat_application.data.network.model.BaseResponse
import dev.open.chat_application.data.network.model.ResultWrapper
import dev.open.chat_application.data.network.model.SignInRequest
import dev.open.chat_application.data.network.model.SignInResponse
import dev.open.chat_application.data.network.model.SignUpRequest
import dev.open.chat_application.data.network.model.SignUpResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signIn(signInRequest: SignInRequest): Flow<ResultWrapper<BaseResponse<SignInResponse>>>
    suspend fun signUp(signUpRequest: SignUpRequest):  Flow<ResultWrapper<BaseResponse<SignUpResponse>>>
}