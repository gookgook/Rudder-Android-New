package com.rudder.src.host.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.model.dto.ChatDto
import com.rudder.model.dto.PartyDto
import com.rudder.model.repository.ChatRepository
import com.rudder.model.repository.PartyRepository
import kotlinx.coroutines.launch

class HostViewModel : ViewModel() {

    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String> = _toastMessage

    private val _hostParties: MutableLiveData<List<PartyDto.Companion.PartyOnlyDate>> =
        MutableLiveData()
    val hostParties: LiveData<List<PartyDto.Companion.PartyOnlyDate>> =
        _hostParties

    private val _selectedHostParty: MutableLiveData<PartyDto.Companion.HostParty> =
        MutableLiveData(PartyDto.Companion.HostParty.from(-1))
    val selectedHostParty: LiveData<PartyDto.Companion.HostParty> =
        _selectedHostParty

    init {
        getHostParties()
    }



    fun getHostParties() {
        viewModelScope.launch {
            val apiResponse = PartyRepository.instance.getHostParties()
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
            _selectedHostParty.value = PartyDto.Companion.HostParty.from(partyId)
            getPartyApplicants()
            getPartyOneToOneChatRooms()
            getPartyDetail()
            getPartyGroupChatRoom()
        }
    }


    fun getPartyApplicants(){
        viewModelScope.launch {
            val apiResponse = PartyRepository.instance.getPartyApplicants(_selectedHostParty.value?.partyId?: return@launch)
            if (apiResponse.code() == 200) {
                val getPartyApplicantsResponse: PartyDto.Companion.GetPartyApplicantsResponse =
                    apiResponse.body() ?: return@launch
                Log.d("getPartyApplicantsResponse",getPartyApplicantsResponse.applicants.toString())
                _selectedHostParty.value?.partyApplicants = getPartyApplicantsResponse.applicants
                _selectedHostParty.value = PartyDto.Companion.HostParty.Companion.from(_selectedHostParty.value?:return@launch)
            }
        }
    }

    fun getPartyOneToOneChatRooms() {
        viewModelScope.launch {
            val apiResponse = ChatRepository.instance.getHostPartyOneToOneChatRooms(_selectedHostParty.value?.partyId?: return@launch)
            if (apiResponse.code() == 200) {
                val getHostPartyOneToOneChatRoomsResponse: ChatDto.Companion.GetHostPartyOneToOneChatRoomsResponse =
                    apiResponse.body() ?: return@launch
                _selectedHostParty.value?.partyOneToOneChatRooms = getHostPartyOneToOneChatRoomsResponse.chatRooms
                _selectedHostParty.value = PartyDto.Companion.HostParty.Companion.from(_selectedHostParty.value?:return@launch)
            }
        }

    }


    fun getPartyGroupChatRoom() {
        viewModelScope.launch {
            val apiResponse = ChatRepository.instance.getPartyGroupChatRoom(_selectedHostParty.value?.partyId?: return@launch)
            if (apiResponse.code() == 200) {
                val partyGroupChatRoom: ChatDto.Companion.PartyGroupChatRoom =
                    apiResponse.body() ?: return@launch
                _selectedHostParty.value?.partyGroupChatRoom = partyGroupChatRoom
                _selectedHostParty.value = PartyDto.Companion.HostParty.Companion.from(_selectedHostParty.value?:return@launch)
            }
        }

    }

    fun getPartyDetail(){
        viewModelScope.launch {

            val apiResponse = PartyRepository.instance.getPartyDetail(PartyDto.Companion.GetPartyDetailRequest(_selectedHostParty.value?.partyId?: return@launch))
            if (apiResponse.code() == 200) {
                val getPartyDetailResponse: PartyDto.Companion.GetPartyDetailResponse =
                    apiResponse.body() ?: return@launch
                _selectedHostParty.value?.partyDetail = getPartyDetailResponse.partyDetail
                _selectedHostParty.value = PartyDto.Companion.HostParty.Companion.from(_selectedHostParty.value?:return@launch)
            }
        }
    }



}