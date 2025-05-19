package dev.open.chat_application.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("status_code")
    val statusCode: String,
    @SerialName("message")
    val message:String?  = null,
    @SerialName("data")
    val data: T
)

@Serializable
data class BaseErrorResponse(
    @SerialName("message")
    val message: String,
    @SerialName("error_code")
    val errorCode: String
)


//  {"message":"User not found, please check the details","error_code":"user_not_found"}
@Serializable
sealed class ResultWrapper<out T> {
    data object Loading : ResultWrapper<Nothing>()
    data class Success<T>(val data: T) : ResultWrapper<T>()
    data class Error(
        val message: String? = null,
        val code: String? = null,
        val exception: Throwable? = null
    ) : ResultWrapper<Nothing>()
}

