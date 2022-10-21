package com.rudder.src.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.model.dto.PartyDto
import com.rudder.model.RetrofitClient
import com.rudder.model.repository.PartyRepository
import kotlinx.coroutines.launch

class PartyApplyViewModel : ViewModel() {

    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String> = _toastMessage

    private val _isApplySuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isApplySuccess: LiveData<Boolean> = _isApplySuccess


    private var _numberApplicants: Int = 0
    private var _partyId:Int? = null


    fun applyParty(onPartyApplySuccess: () -> Unit) {
        viewModelScope.launch {
            val applyPartyRequest = PartyDto.Companion.ApplyPartyRequest(numberApplicants = _numberApplicants)
            _partyId?.let {
                val apiResponse = PartyRepository.instance.applyParty(it,applyPartyRequest)
                if (apiResponse.isSuccessful){
                    onPartyApplySuccess()
                    _isApplySuccess.value=true
                }else{
                    _toastMessage.value = "Network error"
                }
            }

        }
    }

    fun setNumberApplicants(numberApplicants:Int){
        _numberApplicants = numberApplicants
    }

    fun setPartyId(partyId:Int){
        _partyId = partyId
    }



}