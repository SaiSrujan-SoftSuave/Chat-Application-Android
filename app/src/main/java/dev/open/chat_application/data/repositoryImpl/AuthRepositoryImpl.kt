package dev.open.chat_application.data.repositoryImpl

import android.util.Log
import dev.open.chat_application.core.Constants.BASE_URL
import dev.open.chat_application.data.network.ApiEndpoints
import dev.open.chat_application.data.network.model.BaseErrorResponse
import dev.open.chat_application.data.network.model.BaseResponse
import dev.open.chat_application.data.network.model.ResultWrapper
import dev.open.chat_application.data.network.model.SignInRequest
import dev.open.chat_application.data.network.model.SignInResponse
import dev.open.chat_application.data.network.model.SignUpRequest
import dev.open.chat_application.data.network.model.SignUpResponse
import dev.open.chat_application.data.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.reflect.TypeInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(private val client: HttpClient) : AuthRepository {
    override suspend fun signIn(signInRequest: SignInRequest): Flow<ResultWrapper<BaseResponse<SignInResponse>>> =
        flow {
            try {
                emit(ResultWrapper.Loading)
                val response: HttpResponse = client.post("$BASE_URL${ApiEndpoints.SIGN_IN}") {
                    contentType(ContentType.Application.Json)
                    setBody(signInRequest, bodyType = TypeInfo(SignInRequest::class))
                }
                if (response.status.value == 200) {
                    emit(ResultWrapper.Success(response.body()))
                } else {
                    val errorResponse = response.body<BaseErrorResponse>()
                    emit(ResultWrapper.Error(errorResponse.message,errorResponse.errorCode))
                }
            } catch (e: Exception) {
                emit(ResultWrapper.Error(e.message))
                Log.e("TAG", "signIn: ${e.message}")
            }

        }

    override suspend fun signUp(signUpRequest: SignUpRequest): Flow<ResultWrapper<BaseResponse<SignUpResponse>>> =
        flow {
            try {
                emit(ResultWrapper.Loading)
                val response: HttpResponse = client.post("$BASE_URL${ApiEndpoints.SIGN_UP}") {
                    contentType(ContentType.Application.Json)
                    setBody(signUpRequest, bodyType = TypeInfo(SignUpRequest::class))
                }
                if (response.status.value == 200) {
                    emit(ResultWrapper.Success(response.body()))
                } else {
                    val errorResponse = response.body<BaseErrorResponse>()
                    emit(ResultWrapper.Error(errorResponse.message,errorResponse.errorCode))
                }
            } catch (e: Exception) {
                emit(ResultWrapper.Error(e.message))
                Log.e("TAG", "signUp: ${e.message}")
            }

        }


}