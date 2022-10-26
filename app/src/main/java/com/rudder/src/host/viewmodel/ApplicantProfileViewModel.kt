package com.rudder.src.host.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.config.App
import com.rudder.model.dto.*
import com.rudder.model.repository.ChatRepository
import com.rudder.model.repository.PartyRepository
import com.rudder.model.repository.UserRepository
import kotlinx.coroutines.launch

class ApplicantProfileViewModel : ViewModel() {

    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String> = _toastMessage

    private val _partyProfile: MutableLiveData<PartyProfile> = MutableLiveData()
    val partyProfile: LiveData<PartyProfile> = _partyProfile

    lateinit var applicantProfileRequest: ApplicantProfileRequest


    private val _createdChatRoomId: MutableLiveData<Int> = MutableLiveData()
    val createdChatRoomId: LiveData<Int> = _createdChatRoomId



    fun getHostParties() {
        viewModelScope.launch {
            val apiResponse = UserRepository.instance.getPartyProfile(applicantProfileRequest.userInfoId)
            if (apiResponse.code() == 200) {
                val partyProfileResponse: PartyProfileResponse =
                    apiResponse.body() ?: return@launch
                _partyProfile.value = partyProfileResponse.partyProfile

            }
        }
    }

    fun updateApplicantProfileRequest(applicantProfileRequest: ApplicantProfileRequest){
        this.applicantProfileRequest = applicantProfileRequest
    }

    fun createOneToOneChatRoom(){
        viewModelScope.launch {
            val myUserInfoId = if (App.prefs.getValue("userInfoId")
                    .isNullOrEmpty()
            ) return@launch else App.prefs.getValue("userInfoId")?.toInt()?:return@launch
            val apiResponse = ChatRepository.instance.createChatRoom(
                ChatDto.Companion.CreateChatRoomRequest(
                chatRoomItemId = applicantProfileRequest.partyId,
                chatRoomType = "PARTY_ONE_TO_ONE",
                    userInfoIdList = arrayListOf(myUserInfoId,applicantProfileRequest.userInfoId)
            ))
            if (apiResponse.isSuccessful) {
                val createChatRoomResponse: ChatDto.Companion.CreateChatRoomResponse =
                    apiResponse.body() ?: return@launch
                _createdChatRoomId.value = createChatRoomResponse.chatRoomId
                _createdChatRoomId.value = -1
            }
        }
    }




}