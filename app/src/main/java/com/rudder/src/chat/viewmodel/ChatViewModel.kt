package com.rudder.src.chat.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.model.RetrofitClient
import com.rudder.model.dto.ChatDto
import com.rudder.model.service.GetOldChatService
import com.rudder.model.service.SendChatService
import com.rudder.util.SocketHandle.ChatReceivedEvent
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Response

class ChatViewModel : ViewModel() {
    val chatMessages = MutableLiveData<MutableList<ChatDto.Companion.Chat>>(arrayListOf())


    val receivedChatFlag = MutableLiveData<Boolean>(false)

    private var chatRoomId= -1//여따 채널아이디 넣으셈
    val sendChatBody = MutableLiveData<String>()



    fun getOldChat() {
        val getOldChatService: GetOldChatService =
            RetrofitClient.getClient(BuildConfig.BASE_URL).create(GetOldChatService::class.java)
        viewModelScope.launch {
            val getOldChatRequest: Response<ChatDto.Companion.GetOldChatResponse> =
                getOldChatService.getOldChats(chatRoomId, null)
            chatMessages.value?.let {

                chatMessages.postValue(getOldChatRequest.body()!!.chatMessages.toMutableList())

                receivedChatFlag.postValue(true)
            }
        }
    }

    fun sendMessage() {
        Log.d("setChatRoomId2",chatRoomId.toString())
        Log.d("sendChatTest", "sendMessage Touched")


        val sendChatService: SendChatService =
            RetrofitClient.getClient(BuildConfig.BASE_URL).create(SendChatService::class.java)


            viewModelScope.launch {
                sendChatBody.value?.let {
                    if (it == "") return@launch
                    val sendChatRequest: Response<Void> = sendChatService.sendChat(
                        ChatDto.Companion.ChatToSend(
                            "mock",
                            it,
                            chatRoomId,
                            13
                        )
                    )
                    Log.d("sendChatTest", sendChatRequest.code().toString())
                    sendChatBody.value = ""
                }


        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleChat(event: ChatReceivedEvent) {
        val chatMessage = event.chat
        if (chatMessage.chatRoomId != chatRoomId) return

        chatMessages.value?.let {
            Log.d("chatBody", chatMessage.chatMessageBody)

            val copyList = it.toMutableList()
            copyList.add(0, chatMessage)
            chatMessages.postValue(copyList)

            receivedChatFlag.postValue(true)
        }
    }

    fun registerEvent() {
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    fun unregisterEvent() {
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this)
    }

    fun setChatRoomId(chatRoomId: Int) {
        this.chatRoomId = chatRoomId
        Log.d("setChatRoomId1",chatRoomId.toString())
    }

}