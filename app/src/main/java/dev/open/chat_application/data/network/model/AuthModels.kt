package dev.open.chat_application.data.network.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class SignInRequest(
    @SerialName("email_id")
    val email:String,
    val password:String,
)

@Serializable
data class SignUpRequest(
    val name:String,
    @SerialName("email_id")
    val email:String,
    val password:String,
)

@Serializable
data class SignInResponse(
    @SerialName("access_token")
    val accessToken:String,
    @SerialName("user_id")
    @Contextual
    val userId:String,
)

@Serializable
data class SignUpResponse(
    @SerialName("user_id")
    @Contextual
    val userId:String,
)