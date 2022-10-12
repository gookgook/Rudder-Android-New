package com.rudder.util

import android.util.Log
import com.google.gson.Gson
import com.rudder.model.StompSocketClient
import com.rudder.model.dto.ChatDto
import com.rudder.model.dto.SocketMessage
import com.rudder.util.SocketHandle.ChatReceivedEvent
import org.greenrobot.eventbus.EventBus

object StompManager {

    private val stompSocketClient = StompSocketClient.newInstance()

    fun connectSocket(userInfoId: Int) {

        Log.d("init_socket","hehey")

        stompSocketClient.topic("/queue/user." + userInfoId).subscribe{
            it?.let {
                Log.d("socketMessage","come")
                val socketMessage: SocketMessage.Companion.SocketMessage =
                    Gson().fromJson(it.payload, SocketMessage.Companion.SocketMessage::class.java)

                if (socketMessage.socketMessage.messageType == "CHAT_MESSAGE") {
                    val socketMessageChat: SocketMessage.Companion.SocketMessageChat =
                        Gson().fromJson(it.payload, SocketMessage.Companion.SocketMessageChat::class.java)
                    handleChat(socketMessageChat.socketMessage.payload)

                }


            }
        }

        stompSocketClient.connect()

    }

    fun handleChat(chatMessage: ChatDto.Companion.Chat) {
        EventBus.getDefault().post(ChatReceivedEvent(chatMessage))
    }
}

