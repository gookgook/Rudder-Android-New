package com.rudder.src.main.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.config.App
import com.rudder.model.RetrofitClient
import com.rudder.model.dto.PartyProfile
import com.rudder.model.dto.PartyProfileResponse
import com.rudder.model.service.GetPartyProfileService
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.create

class MyProfileViewModel: ViewModel() {

    val toastMessage = MutableLiveData<String>()

    var partyProfile = MutableLiveData<PartyProfile>()


    fun getProfile(){
        val getPartyProfileService: GetPartyProfileService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(GetPartyProfileService::class.java)

        viewModelScope.launch {
            val userInfoId = App.prefs.getValue("userInfoId")!!
            val getPartyProfileRequest: Response<PartyProfileResponse> = getPartyProfileService.getPartyProfile(userInfoId = userInfoId)
            when (getPartyProfileRequest.code()) {
                200 -> partyProfile.value = getPartyProfileRequest.body()!!.partyProfile
                else -> toastMessage.value = "Server Error"
            }
        }
    }
}