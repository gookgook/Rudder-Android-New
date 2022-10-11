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
        ){
            companion object{
                val mock = PartyGroupChatRoom(
                    chatRoomId = -1,
                    chatRoomImageUrl = "",
                    chatRoomTitle = "",
                    notReadMessageCount = 0,
                    recentMessage = "",
                    recentMessageTime = Timestamp(System.currentTimeMillis())
                )
            }
        }

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


        data class GetHostPartyOneToOneChatRoomsResponse(
            val chatRooms: List<PartyOneToOneChatRoom>
        )

        data class Chat(
            val chatMessageId: Int,
            val chatMessageBody: String,
            val chatMessageTime : String,
            val sendUserInfoId: Int,
            val sendUserNickname: String,
            val isMine: Boolean,
            val chatRoomId: Int
        )

        data class GetOldChatResponse(
            val chatMessages: List<Chat>
        )

        data class ChatToSend(

            val sender : String,
            val body: String,
            val channelId: Int,
            val sendTime: Int
        )

    }
}