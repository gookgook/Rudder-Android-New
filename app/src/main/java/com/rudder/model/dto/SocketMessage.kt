package com.rudder.model.dto

class SocketMessage {
    companion object{

        data class SocketMessage(
            val socketMessage: PayloadContent
        )

        data class SocketMessageChat(
            val socketMessage: PayloadContentChat
        )

        data class  SocketMessageNotification(
            val socketMessage: PayloadContentUserNotification
        )

        data class PayloadContent(
            val messageType: String,
            val payload: Any
        )

        data class PayloadContentChat(
            val messageType: String,
            val payload: ChatDto.Companion.Chat
        )

        data class PayloadContentUserNotification(
            val messageType: String,
            val payload: UserNotification
        )
    }
}


