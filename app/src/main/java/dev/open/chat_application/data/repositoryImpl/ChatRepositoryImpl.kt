package dev.open.chat_application.data.repositoryImpl

import android.util.Log
import dev.open.chat_application.core.Constants.BASE_URL
import dev.open.chat_application.data.network.ApiEndpoints
import dev.open.chat_application.data.network.model.BaseErrorResponse
import dev.open.chat_application.data.network.model.BaseResponse
import dev.open.chat_application.data.network.model.ResultWrapper
import dev.open.chat_application.data.network.model.SignInRequest
import dev.open.chat_application.data.network.model.UserData
import dev.open.chat_application.data.network.model.UserModel
import dev.open.chat_application.data.repository.ChatRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.reflect.TypeInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ChatRepositoryImpl(
    private val client: HttpClient
) : ChatRepository {
    override fun getAllUsers(): Flow<ResultWrapper<BaseResponse<UserData>>> =
        flow {
            try {
                emit(ResultWrapper.Loading)
                val response: HttpResponse = client.get("$BASE_URL${ApiEndpoints.USERS}") {
                    contentType(ContentType.Application.Json)
//                setBody(signInRequest, bodyType = TypeInfo(SignInRequest::class))
                }
                if (response.status.value == 200) {
                    val res : BaseResponse<UserData> = response.body()
                    emit(ResultWrapper.Success(res))
                } else {
                    val errorResponse = response.body<BaseErrorResponse>()
                    emit(ResultWrapper.Error(errorResponse.message, errorResponse.errorCode))
                }
            } catch (e: Exception) {
                emit(ResultWrapper.Error(e.message))
                Log.e("TAG", "signIn: ${e.message}")
            }
        }
}