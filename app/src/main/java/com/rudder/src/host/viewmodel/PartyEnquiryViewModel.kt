package com.rudder.src.host.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.model.RetrofitClient
import com.rudder.model.dto.PartyEnquiryInfo
import com.rudder.model.service.PartyEnquiryService
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.create

class PartyEnquiryViewModel: ViewModel() {
    val enquiryBody: MutableLiveData<String> = MutableLiveData()

    val enquiryResultFlag: MutableLiveData<Int> = MutableLiveData()

    fun sendEnquiry(partyId: Int, customerSoundType: String) {

        val partyEnquiryService: PartyEnquiryService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(PartyEnquiryService::class.java)

        viewModelScope.launch {
            val partyEnquiryInfo: PartyEnquiryInfo = PartyEnquiryInfo(enquiryBody.value!!,customerSoundType,partyId)
            val partyEnquiryRequest: Response<Void> = partyEnquiryService.sendPartyEnquiry(partyEnquiryInfo)
            when (partyEnquiryRequest.code()) {
                204 -> enquiryResultFlag.value = 1
                else -> enquiryResultFlag.value = -1
            }
        }
    }
}