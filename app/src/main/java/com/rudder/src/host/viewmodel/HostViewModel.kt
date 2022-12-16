package com.rudder.src.host.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.model.dto.ChatDto
import com.rudder.model.dto.HostParty
import com.rudder.model.dto.PartyDto
import com.rudder.model.repository.ChatRepository
import com.rudder.model.repository.PartyRepository
import com.rudder.util.SocketHandle.ChatReceivedEvent
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HostViewModel : ViewModel() {

    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String> = _toastMessage

    private val _hostParties: MutableLiveData<List<PartyDto.Companion.PartyOnlyDate>> =
        MutableLiveData()
    val hostParties: LiveData<List<PartyDto.Companion.PartyOnlyDate>> =
        _hostParties

    private val _selectedHostParty: MutableLiveData<HostParty> =
        MutableLiveData(HostParty.from(-1))
    val selectedHostParty: LiveData<HostParty> =
        _selectedHostParty

    val isLoadingFlag = MutableLiveData<Boolean> (false)

    init {

        getHostParties()
    }



    fun getHostParties() {
        viewModelScope.launch {
            isLoadingFlag.value = true
            val apiResponse = PartyRepository().getHostParties()
            isLoadingFlag.value = false
            if (apiResponse.code() == 200) {
                val getHostPartiesResponse: PartyDto.Companion.GetHostPartiesResponse =
                    apiResponse.body() ?: PartyDto.Companion.GetHostPartiesResponse(
                        arrayListOf()
                    )

                _hostParties.value = getHostPartiesResponse.parties




            }
        }
    }

    fun setSelectedParty(partyId: Int) {
        viewModelScope.launch {

//            if (partyId.equals(selectedHostParty.value?.partyId)) return@launch
            if (partyId.equals(-1)) return@launch
            _selectedHostParty.value = HostParty.from(partyId)
            getPartyApplicants()
            getPartyOneToOneChatRooms()
            getPartyDetail()
            getPartyGroupChatRoom()
            isLoadingFlag.value = false
        }
    }


    fun getPartyApplicants(){
        viewModelScope.launch {
            isLoadingFlag.value = true
            val apiResponse = PartyRepository().getPartyApplicants(_selectedHostParty.value?.partyId?: return@launch)
            isLoadingFlag.value = false
            if (apiResponse.code() == 200) {
                val getPartyApplicantsResponse: PartyDto.Companion.GetPartyApplicantsResponse =
                    apiResponse.body() ?: return@launch
                Log.d("getPartyApplicantsResponse",getPartyApplicantsResponse.applicants.toString())
                _selectedHostParty.value?.partyApplicants = getPartyApplicantsResponse.applicants
                _selectedHostParty.value = HostParty.Companion.from(_selectedHostParty.value?:return@launch)
            }
        }
    }

    fun getPartyOneToOneChatRooms() {
        viewModelScope.launch {
            isLoadingFlag.value = true
            val apiResponse = ChatRepository().getHostPartyOneToOneChatRooms(_selectedHostParty.value?.partyId?: return@launch)
            isLoadingFlag.value = false
            if (apiResponse.code() == 200) {
                val getHostPartyOneToOneChatRoomsResponse: ChatDto.Companion.GetHostPartyOneToOneChatRoomsResponse =
                    apiResponse.body() ?: return@launch
                _selectedHostParty.value?.partyOneToOneChatRooms = getHostPartyOneToOneChatRoomsResponse.chatRooms
                _selectedHostParty.value = HostParty.Companion.from(_selectedHostParty.value?:return@launch)
            }
        }

    }


    fun getPartyGroupChatRoom() {
        viewModelScope.launch {
            isLoadingFlag.value = true
            val apiResponse = ChatRepository().getPartyGroupChatRoom(_selectedHostParty.value?.partyId?: return@launch)
            isLoadingFlag.value = false
            if (apiResponse.code() == 200) {
                val partyGroupChatRoom: ChatDto.Companion.PartyGroupChatRoom =
                    apiResponse.body() ?: return@launch
                _selectedHostParty.value?.partyGroupChatRoom = partyGroupChatRoom
                _selectedHostParty.value = HostParty.Companion.from(_selectedHostParty.value?:return@launch)
            }
        }

    }

    fun getPartyDetail(){
        viewModelScope.launch {
            isLoadingFlag.value = true
            val apiResponse = PartyRepository().getPartyDetail(PartyDto.Companion.GetPartyDetailRequest(_selectedHostParty.value?.partyId?: return@launch))
            isLoadingFlag.value = false
            if (apiResponse.code() == 200) {
                val getPartyDetailResponse: PartyDto.Companion.GetPartyDetailResponse =
                    apiResponse.body() ?: return@launch
                _selectedHostParty.value?.partyDetail = getPartyDetailResponse.partyDetail
                _selectedHostParty.value = HostParty.Companion.from(_selectedHostParty.value?:return@launch)
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleChat(event: ChatReceivedEvent) {
        val chatMessage = event.chat
        if (_selectedHostParty.value?.partyGroupChatRoom?.chatRoomId?.equals(chatMessage.chatRoomId) == true){
            _selectedHostParty.value?.partyGroupChatRoom?.receiveNewMessage(chatMessage)
        }
        _selectedHostParty.value?.partyOneToOneChatRooms?.forEach {
            if (it.chatRoomId.equals(chatMessage.chatRoomId)){
                it.receiveNewMessage(chatMessage)
            }
        }
        val copiedHostParty = HostParty.from(_selectedHostParty.value?:return)
        _selectedHostParty.value = copiedHostParty
    }


    fun registerEvent() {
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    fun unregisterEvent() {
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this)
    }
    \
}