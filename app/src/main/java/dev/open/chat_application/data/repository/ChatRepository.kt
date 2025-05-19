package dev.open.chat_application.data.repository

import dev.open.chat_application.data.network.model.BaseResponse
import dev.open.chat_application.data.network.model.ResultWrapper
import dev.open.chat_application.data.network.model.UserData
import dev.open.chat_application.data.network.model.UserModel
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun getAllUsers() : Flow<ResultWrapper<BaseResponse<UserData>>>
}