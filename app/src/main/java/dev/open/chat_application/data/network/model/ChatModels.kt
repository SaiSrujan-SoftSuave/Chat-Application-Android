package dev.open.chat_application.data.network.model

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    @SerialName("users")
    val users : List<UserModel>? = null
)

@Serializable
@Keep
data class UserModel(
    @SerialName("email")
    val email: String,
    @SerialName("id")
    val id: String,
    @SerialName("is_deleted")
    val isDeleted: Boolean,
    @SerialName("is_online")
    val isOnline: Boolean,
    @SerialName("name")
    val name: String
)
