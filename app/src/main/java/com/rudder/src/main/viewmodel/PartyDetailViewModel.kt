package com.rudder.src.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.model.dto.PartyDto
import com.rudder.model.RetrofitClient
import com.rudder.model.repository.PartyRepository
import kotlinx.coroutines.launch

class PartyDetailViewModel : ViewModel() {

    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String> = _toastMessage

    private val _partyDetail: MutableLiveData<PartyDto.Companion.PartyDetail> = MutableLiveData()
    val partyDetail: LiveData<PartyDto.Companion.PartyDetail> = _partyDetail



    fun getPartyDetail(partyId: Int) {
        viewModelScope.launch {
            val getPartyDetailRequest = PartyDto.Companion.GetPartyDetailRequest(partyId = partyId)
            val apiResponse = PartyRepository.instance.getPartyDetail(getPartyDetailRequest)
            if (apiResponse.code()==200){
                val getPartyDetailResponse: PartyDto.Companion.GetPartyDetailResponse = apiResponse.body()?: return@launch

                _partyDetail.value = getPartyDetailResponse.partyDetail

            }else{
                _toastMessage.value = "Network error"
            }
        }
    }


}