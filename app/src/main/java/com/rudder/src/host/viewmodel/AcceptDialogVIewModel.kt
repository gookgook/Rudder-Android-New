package com.rudder.src.host.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.model.RetrofitClient
import com.rudder.model.dto.AcceptApplicantRequest
import com.rudder.model.service.AcceptApplicantService
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.create

class AcceptDialogVIewModel: ViewModel() {

    var acceptResultFlag = MutableLiveData<Int> ()

    fun acceptApplicant(partyId: Int, partyMemberId: Int) {

        val acceptApplicantRequest = AcceptApplicantRequest(partyMemberId)
        val acceptApplicantService: AcceptApplicantService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(AcceptApplicantService::class.java)
        viewModelScope.launch {


            val acceptRequest: Response<Void> = acceptApplicantService.acceptApplicant(partyId,acceptApplicantRequest)

            when(acceptRequest.code()) {
                200 -> acceptResultFlag.value = 1
                else -> acceptResultFlag.value = -1

            }

        }
    }
}