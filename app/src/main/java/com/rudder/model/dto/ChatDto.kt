package com.rudder.model.dto

import java.io.File
import java.sql.Timestamp

class ChatDto {
    companion object {


        data class PartyGroupChatRoom(
            val chatRoomId: Int,
            val chatRoomImageUrl: String,
            val chatRoomTitle: String,
            val notReadMessageCount: Int,
            val recentMessage: String = "",
            val recentMessageTime: Timestamp
        )

        data class PartyOneToOneChatRoom(
            val chatRoomId: Int,
            val chatRoomImageUrl: String,
            val chatRoomTitle: String,
            val notReadMessageCount: Int,
            val recentMessage: String = "",
            val recentMessageTime: Timestamp,
            val otherUserInfoId: Int,
            val partyId: Int
        )

        data class GetApplicationPartyOneToOneChatRoomsResponse(
            val chatRooms: List<PartyOneToOneChatRoom>
        )

    }
}