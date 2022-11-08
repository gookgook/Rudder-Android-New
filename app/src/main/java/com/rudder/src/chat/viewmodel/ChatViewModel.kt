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


    var endChatMessageId: Int = -1

    val receivedChatFlag = MutableLiveData<Boolean>(false)
    val scrollToBottomFlag = MutableLiveData<Boolean>(false)

    var isRefreshingFlag : Boolean = false

    private var chatRoomId= -1//여따 채널아이디 넣으셈
    val sendChatBody = MutableLiveData<String>()

    val isLoadingFlag = MutableLiveData<Boolean> (false)



    fun getOldChat(isMore: Boolean=false) {
        val getOldChatService: GetOldChatService =
            RetrofitClient.getClient(BuildConfig.BASE_URL).create(GetOldChatService::class.java)
        viewModelScope.launch {

            isLoadingFlag.value = true
            var getOldChatRequest: Response<ChatDto.Companion.GetOldChatResponse>
            if (!isMore) {
                getOldChatRequest = getOldChatService.getOldChats(chatRoomId)
            } else {
                getOldChatRequest = getOldChatService.getOldChats(chatRoomId, endChatMessageId)
            }
            isLoadingFlag.value = false

            chatMessages.value?.let {

                if (!isMore) {
                    chatMessages.postValue(getOldChatRequest.body()!!.chatMessages.toMutableList())

                } else {
                    it.addAll(getOldChatRequest.body()!!.chatMessages.toMutableList())
                    chatMessages.postValue(it)
                }

                if(getOldChatRequest.body()!!.chatMessages.isNotEmpty()){
                    endChatMessageId = getOldChatRequest.body()!!.chatMessages.last().chatMessageId
                }
                //scrollToBottomFlag.postValue(true)
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
                    isLoadingFlag.value = true
                    val sendChatRequest: Response<Void> = sendChatService.sendChat(
                        ChatDto.Companion.ChatToSend(
                            "mock",
                            it,
                            chatRoomId,
                            13
                        )
                    )
                    Log.d("sendChatTest", sendChatRequest.code().toString())
                    isLoadingFlag.value = false
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

            scrollToBottomFlag.value = true
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