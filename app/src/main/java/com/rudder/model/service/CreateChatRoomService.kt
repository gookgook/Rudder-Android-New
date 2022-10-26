package com.rudder.model.service

import com.rudder.model.dto.ChatDto
import com.rudder.model.dto.PartyDto
import retrofit2.Response
import retrofit2.http.*

interface CreateChatRoomService {
    @POST("/chat-rooms")
    suspend fun createChatRoom(
        @Body createChatRoomRequest: ChatDto.Companion.CreateChatRoomRequest
    ): Response<ChatDto.Companion.CreateChatRoomResponse>



}