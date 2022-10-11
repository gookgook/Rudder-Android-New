package com.rudder.src.chat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.rudder.BuildConfig
import com.rudder.R
import com.rudder.config.App
import com.rudder.model.RetrofitClient
import com.rudder.model.StompSocketClient
import com.rudder.model.dto.ChatDto
import com.rudder.model.dto.SocketMessage
import com.rudder.model.service.GetOldChatService
import com.rudder.model.service.LoginService
import com.rudder.model.service.SendChatService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.create
import java.sql.Timestamp

class ChatViewModel: ViewModel() {
    val chatMessages = MutableLiveData<MutableList<ChatDto.Companion.Chat>>(arrayListOf())
    private val currentChatRoomId = MutableLiveData<Long>()

    val sendChatMessageBody = MutableLiveData<String>()
    private val stompSocketClient = StompSocketClient.newInstance()

    val receivedChatFlag = MutableLiveData<Boolean>(false)

    val channelId = 54 //여따 채널아이디 넣으셈
    val chatBody = MutableLiveData<String>()


    fun connectChatRoomSocket() {

        val userInfoId = App.prefs.getValue("userInfoId")!!
        stompSocketClient.topic("/queue/user." + userInfoId).subscribe{
            it?.let {
                Log.d("socketMessage","come")
                val socketMessage: SocketMessage.Companion.SocketMessage =
                    Gson().fromJson(it.payload, SocketMessage.Companion.SocketMessage::class.java)
                handleChat(socketMessage.socketMessage.payload)
            }
        }

        stompSocketClient.connect()

    }

    fun getOldChat(){
        val getOldChatService: GetOldChatService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(GetOldChatService::class.java)
        viewModelScope.launch {
            val getOldChatRequest: Response<ChatDto.Companion.GetOldChatResponse> = getOldChatService.getOldChats(channelId, "-1")
            chatMessages.value?.let {

                chatMessages.postValue(getOldChatRequest.body()!!.chatMessages.toMutableList())

                receivedChatFlag.postValue(true)
            }
        }
    }

    private fun handleChat(customMessage: ChatDto.Companion.Chat) {

        chatMessages.value?.let {
            Log.d("chatBody",customMessage.chatMessageBody)

            val copyList = it.toMutableList()
            copyList.add(0,customMessage)
            chatMessages.postValue(copyList)

            receivedChatFlag.postValue(true)
        }
    }

    fun sendMessage() {

        Log.d("sendChatTest","sendMessage Touched")
        currentChatRoomId.value = 54

        val sendChatService: SendChatService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(SendChatService::class.java)

        currentChatRoomId.value?.let { currentChatRoomId ->
            viewModelScope.launch {
                chatBody.value?.let {
                    if(it == "") return@launch
                    val sendChatRequest: Response<Void> = sendChatService.sendChat(ChatDto.Companion.ChatToSend("mock",it,channelId,13))
                    Log.d("sendChatTest",sendChatRequest.code().toString())
                    chatBody.value = ""
                }
            }

        }
    }


}