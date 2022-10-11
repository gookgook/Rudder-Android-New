package com.rudder.model.service

import com.rudder.model.dto.ChatDto
import com.rudder.model.dto.PartyDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GetChatRoomsService {
    @GET("/chat-rooms/party-group/{partyId}")
    suspend fun getParties(
        @Path("partyId") partyId: Int
    ): Response<ChatDto.Companion.PartyGroupChatRoom>

    @GET("/chat-rooms/party-one-to-one")
    suspend fun getApplicationOneToOneChatRooms(

    ): Response<ChatDto.Companion.GetApplicationPartyOneToOneChatRoomsResponse>


}