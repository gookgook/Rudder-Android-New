package com.rudder.model.repository

import com.rudder.BuildConfig
import com.rudder.model.RetrofitClient
import com.rudder.model.dto.ChatDto
import com.rudder.model.service.GetChatRoomsService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Response

class ChatRepository {

    companion object{
        val instance = ChatRepository()
    }

    private val getChatRoomsService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(
        GetChatRoomsService::class.java)



    suspend fun getPartyGroupChatRoom(partyId: Int): Response<ChatDto.Companion.PartyGroupChatRoom> {

        return CoroutineScope(Dispatchers.IO).async {
            getChatRoomsService.getPartyGroupChatRoom(partyId = partyId)
        }.await()
    }

    suspend fun getApplicationOneToOneChatRooms(): Response<ChatDto.Companion.GetApplicationPartyOneToOneChatRoomsResponse> {

        return CoroutineScope(Dispatchers.IO).async {
            getChatRoomsService.getApplicationOneToOneChatRooms()
        }.await()
    }

    suspend fun getHostPartyOneToOneChatRooms(partyId: Int): Response<ChatDto.Companion.GetHostPartyOneToOneChatRoomsResponse> {

        return CoroutineScope(Dispatchers.IO).async {
            getChatRoomsService.getHostPartyOneToOneChatRooms(partyId = partyId)
        }.await()
    }



}