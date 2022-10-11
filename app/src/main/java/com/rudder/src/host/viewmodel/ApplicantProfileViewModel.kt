package com.rudder.src.host.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private lateinit var applicantProfileRequest: ApplicantProfileRequest




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

    fun setApplicantProfileRequest(applicantProfileRequest: ApplicantProfileRequest){
        this.applicantProfileRequest = applicantProfileRequest
    }




}