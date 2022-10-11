package com.rudder.model.service

import com.rudder.model.dto.ChatDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SendChatService {
    @POST("/send-chat")
    suspend fun sendChat(@Body customMessage: ChatDto.Companion.ChatToSend): Response<Void>
}