package com.rudder.util

import android.content.ContentValues
import android.media.metrics.Event
import android.util.Log
import com.google.gson.Gson
import com.rudder.model.StompSocketClient
import com.rudder.model.dto.ChatDto
import com.rudder.model.dto.SocketMessage
import com.rudder.util.SocketHandle.ChatReceivedEvent
import io.reactivex.disposables.Disposable
import com.rudder.util.SocketHandle.NotificationReceivedEvent
import org.greenrobot.eventbus.EventBus

object StompManager {

    private val stompSocketClient = StompSocketClient.newInstance()
    private lateinit var disposable: Disposable

    fun connectSocket(userInfoId: Int) {


        if (!this::disposable.isInitialized){
            disposable = stompSocketClient.topic("/queue/user." + userInfoId).subscribe {
                it?.let {
                    Log.d("socketMessage", "come")

                    Log.d(ContentValues.TAG, it.toString())
                    val socketMessage: SocketMessage.Companion.SocketMessage =
                        Gson().fromJson(it.payload, SocketMessage.Companion.SocketMessage::class.java)

                    if (socketMessage.socketMessage.messageType == "CHAT_MESSAGE") {
                        val socketMessageChat: SocketMessage.Companion.SocketMessageChat =
                            Gson().fromJson(
                                it.payload,
                                SocketMessage.Companion.SocketMessageChat::class.java
                            )
                        handleChat(socketMessageChat.socketMessage.payload)

                    }

                    if (socketMessage.socketMessage.messageType == "NOTIFICATION") {
                        val socketMessageNotification: SocketMessage.Companion.SocketMessageNotification =
                            Gson().fromJson(it.payload, SocketMessage.Companion.SocketMessageNotification::class.java)
                        handleNotification()
                    }


                }
            }

            stompSocketClient.withServerHeartbeat(5000)
            stompSocketClient.withClientHeartbeat(5000)

            stompSocketClient.connect()

        }


    }

    fun handleChat(chatMessage: ChatDto.Companion.Chat) {
        EventBus.getDefault().post(ChatReceivedEvent(chatMessage))
    }

    fun handleNotification() {
        EventBus.getDefault().post(NotificationReceivedEvent())
    }
}

