package com.rudder.model.service

import com.rudder.model.dto.ChatDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetOldChatService {

    @GET("/chat-messages/{chatRoomId}}")
    suspend fun getOldChats(
        @Path("chatRoomId") chatRoomId: Int,
        @Query("endChatMessageId") endChatMessageId: String
    ): Response<ChatDto.Companion.GetOldChatResponse>
}