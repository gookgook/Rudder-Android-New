package com.rudder.model.service

import com.rudder.model.dto.ChatDto
import com.rudder.model.dto.PartyDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CreateChatRoomService {
    @GET("/chat-rooms")
    suspend fun createChatRoom(
        @Body createChatRoomRequest: ChatDto.Companion.CreateChatRoomRequest
    ): Response<ChatDto.Companion.CreateChatRoomResponse>



}