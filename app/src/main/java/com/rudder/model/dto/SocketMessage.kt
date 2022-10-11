package com.rudder.model.dto

class SocketMessage {
    companion object{

        data class SocketMessage(
            val socketMessage: PayloadContent
        )

        data class PayloadContent(
            val messageType: String,
            val payload: ChatDto.Companion.Chat
        )
    }
}
