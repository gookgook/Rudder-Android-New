package com.rudder.src.application.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.model.dto.ChatDto
import com.rudder.model.dto.PartyDto
import com.rudder.model.repository.ChatRepository
import com.rudder.model.repository.PartyRepository
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.util.stream.Collectors

class ApplicationViewModel : ViewModel() {

    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String> = _toastMessage

    private val _approvedPartyItems: MutableLiveData<Map<Int, PartyDto.Companion.ApprovedPartyItem>> =
        MutableLiveData()
    val approvedPartyItems: LiveData<Map<Int, PartyDto.Companion.ApprovedPartyItem>> =
        _approvedPartyItems

    private val _appliedPartyItems: MutableLiveData<Map<Int, PartyDto.Companion.AppliedPartyItem>> =
        MutableLiveData()
    val appliedPartyItems: LiveData<Map<Int, PartyDto.Companion.AppliedPartyItem>> =
        _appliedPartyItems

    init {
        getApprovedParty()
        getAppliedParty()
    }

    fun getApprovedParty() {
        viewModelScope.launch {
            val apiResponse = PartyRepository.instance.getApprovedParties()
            if (apiResponse.code() == 200) {
                val getApprovedPartyResponse: PartyDto.Companion.GetApprovedPartyResponse =
                    apiResponse.body() ?: PartyDto.Companion.GetApprovedPartyResponse(
                        arrayListOf()
                    )
                val approvedPartyItemsMap: Map<Int, PartyDto.Companion.ApprovedPartyItem> =
                    getApprovedPartyResponse.parties.stream()
                        .collect(Collectors.toMap(
                            PartyDto.Companion.PartyPreview::partyId,
                            { partyPreview ->
                                PartyDto.Companion.ApprovedPartyItem(
                                    party = partyPreview,
                                    partyGroupChatRoom = ChatDto.Companion.PartyGroupChatRoom(
                                        -1, "", "", 0, "",
                                        Timestamp(System.currentTimeMillis())
                                    )
                                )
                            }
                        )) as Map<Int, PartyDto.Companion.ApprovedPartyItem>
                _approvedPartyItems.value = approvedPartyItemsMap

                _approvedPartyItems.value?.let {
                    for (entry in it) {
                        getPartyGroupChat(entry.value.party.partyId)
                    }
                }

            }
        }
    }

    fun getAppliedParty() {
        viewModelScope.launch {
            val apiResponse = PartyRepository.instance.getAppliedParties()
            if (apiResponse.code() == 200) {
                val getAppliedPartyResponse: PartyDto.Companion.GetAppliedPartyResponse =
                    apiResponse.body() ?: PartyDto.Companion.GetAppliedPartyResponse(
                        arrayListOf()
                    )
                val appliedPartyItemsMap: Map<Int, PartyDto.Companion.AppliedPartyItem> =
                    getAppliedPartyResponse.parties.stream()
                        .collect(Collectors.toMap(
                            PartyDto.Companion.PartyPreview::partyId,
                            { partyPreview ->
                                PartyDto.Companion.AppliedPartyItem(
                                    party = partyPreview,
                                    partyOneToOneChatRoom = ChatDto.Companion.PartyOneToOneChatRoom(
                                        -1, "", "", 0, "",
                                        Timestamp(System.currentTimeMillis()), -1,-1
                                    )
                                )
                            }
                        )) as Map<Int, PartyDto.Companion.AppliedPartyItem>
                _appliedPartyItems.value = appliedPartyItemsMap


                getApplicationPartyOneToOneChatRooms()


            }
        }
    }

    fun getPartyGroupChat(partyId: Int) {
        viewModelScope.launch {
            val apiResponse = ChatRepository.instance.getPartyGroupChatRoom(partyId)
            if (apiResponse.isSuccessful) {
                val partyGroupChatRoom = apiResponse.body() ?: return@launch
                val copyMap = HashMap(_approvedPartyItems.value)
                val newApprovedPartyItem = PartyDto.Companion.ApprovedPartyItem(
                    party = copyMap[partyId]?.party ?: return@launch,
                    partyGroupChatRoom = partyGroupChatRoom
                )
                copyMap[partyId] = newApprovedPartyItem
                _approvedPartyItems.value = copyMap
            }
        }
    }

    fun getApplicationPartyOneToOneChatRooms() {
        viewModelScope.launch {
            val apiResponse = ChatRepository.instance.getApplicationOneToOneChatRooms()
            if (apiResponse.isSuccessful) {
                val partyOneToOneChatRooms = apiResponse.body()?.chatRooms ?: return@launch
                val copyMap = HashMap(_appliedPartyItems.value)
                partyOneToOneChatRooms.forEach { partyOneToOneChatRoom ->
                    run {
                        val newAppliedPartyItem = PartyDto.Companion.AppliedPartyItem(
                            party = copyMap[partyOneToOneChatRoom.partyId]?.party ?: return@forEach,
                            partyOneToOneChatRoom = partyOneToOneChatRoom
                        )
                        copyMap[partyOneToOneChatRoom.partyId] = newAppliedPartyItem

                    }
                }



                _appliedPartyItems.value = copyMap
            }
        }
    }


}