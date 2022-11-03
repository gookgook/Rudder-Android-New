package com.rudder.src.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.model.dto.PartyDto
import com.rudder.model.RetrofitClient
import com.rudder.model.repository.PartyRepository
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class PartyMainViewModel : ViewModel() {

    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String> = _toastMessage

    private val _partyPreviewList: MutableLiveData<List<PartyDto.Companion.PartyPreview>> = MutableLiveData()
    val partyPreviewList: LiveData<List<PartyDto.Companion.PartyPreview>> = _partyPreviewList

    private val getPartyRequest: PartyDto.Companion.GetPartiesRequest = PartyDto.Companion.GetPartiesRequest(endPartyId = null)

    val isLoadingFlag = MutableLiveData<Boolean> (false)

    val newNoticationFlag = MutableLiveData<Boolean> (false)
    init {
        getParties()
    }

    fun getParties(isMore: Boolean=false) {
        viewModelScope.launch {
            isLoadingFlag.value = true
            val apiResponse = PartyRepository().getParties(getPartyRequest)
            isLoadingFlag.value = false
            if (apiResponse.code()==200){
                val getPartiesResponse: PartyDto.Companion.GetPartiesResponse = apiResponse.body()?: PartyDto.Companion.GetPartiesResponse(
                    arrayListOf()
                )
                if (isMore){
                    val copyList = _partyPreviewList.value?.toMutableList()
                    copyList?.addAll(getPartiesResponse.parties)
                    copyList?.let {
                        _partyPreviewList.value = it
                    }
                }else{
                    _partyPreviewList.value = getPartiesResponse.parties
                }
                if(getPartiesResponse.parties.isNotEmpty()){
                    getPartyRequest.endPartyId = getPartiesResponse.parties.last().partyId
                }else{
                    _toastMessage.value = "No more parties"
                }

            }
        }
    }

    fun refreshParties(){
        getPartyRequest.endPartyId = null
        getParties()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun handleNotification(){
        newNoticationFlag.value = true
    }
}