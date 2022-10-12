package com.rudder.src.host.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.model.RetrofitClient
import com.rudder.model.service.CancelPartyService
import com.rudder.model.service.FixMemberService
import com.rudder.model.service.LoginService
import com.rudder.model.service.StopRecruitService
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.create

class PartySettingViewModel: ViewModel() {

    val cancelPartyFlag = MutableLiveData<Int> ()
    val stopRecruitFlag = MutableLiveData<Int> ()
    val fixMemberFlag = MutableLiveData<Int> ()

    fun cancelParty(partyId: Int){
        val cancelPartyService: CancelPartyService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(CancelPartyService::class.java)
        viewModelScope.launch {
            val cancelPartyRequest: Response<Void> = cancelPartyService.cancelParty(partyId)
            when(cancelPartyRequest.code()) {
                204 -> cancelPartyFlag.value = 1
                406 -> cancelPartyFlag.value = 2
                else -> cancelPartyFlag.value = -1
            }
        }
    }

    fun stopRecruit(partyId: Int){
        val stopRecruitService: StopRecruitService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(StopRecruitService::class.java)
        viewModelScope.launch {
            val stopRecruitRequest: Response<Void> = stopRecruitService.stopRecruit(partyId)
            when(stopRecruitRequest.code()) {
                204 -> stopRecruitFlag.value = 1
                406 -> stopRecruitFlag.value = 2
                else -> stopRecruitFlag.value = -1
            }
        }
    }

    fun fixMembers(partyId: Int){
        val fixMemberService: FixMemberService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(FixMemberService::class.java)
        viewModelScope.launch {
            val fixMemberRequest: Response<Void> = fixMemberService.fixMembers(partyId)
            when(fixMemberRequest.code()) {
                204 -> fixMemberFlag.value = 1
                else -> fixMemberFlag.value = -1
            }
        }
    }


}