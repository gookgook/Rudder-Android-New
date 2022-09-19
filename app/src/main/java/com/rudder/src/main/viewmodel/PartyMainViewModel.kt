package com.rudder.src.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.model.dto.PartyDto
import com.rudder.model.RetrofitClient
import com.rudder.model.repository.PartyRepository
import kotlinx.coroutines.launch

class PartyMainViewModel : ViewModel() {

    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String> = _toastMessage

    private val _partyPreviewList: MutableLiveData<List<PartyDto.Companion.PartyPreview>> = MutableLiveData()
    val partyPreviewList: LiveData<List<PartyDto.Companion.PartyPreview>> = _partyPreviewList

    private val getPartyRequest: PartyDto.Companion.GetPartiesRequest = PartyDto.Companion.GetPartiesRequest(endPartyId = null)

    fun getParties(isMore: Boolean=false) {
        RetrofitClient.updateAuthToken("eyJhbGciOiJIUzUxMiJ9.eyJwcm9tb3Rpb25NYWlsQWdyZWVtZW50IjpmYWxzZSwiYXV0aCI6IlJPTEVfVVNFUiIsInNjaG9vbCI6eyJzY2hvb2xJZCI6NCwic2Nob29sTmFtZSI6Im5hdmVyIiwicmVnZXgiOiJtaHBhcmswMjIwQG5hdmVyLmNvbSJ9LCJ1c2VyTmlja25hbWUiOiJhZG1pbm0iLCJ1c2VyRW1haWwiOiJtaHBhcmswMjIwQG5hdmVyLmNvbSIsInVzZXJUeXBlIjowLCJ1c2VySWQiOiJtaHBhcmswMjIwQG5hdmVyLmNvbSIsInVzZXJJbmZvSWQiOjM0Nywibm90aWZpY2F0aW9uVG9rZW4iOiJyaWdodENhc2UifQ.c5ByggYQazfIK1tf0TvFf7Zg3VH4nWoQtX3o_9DV9rSa9uovGC9G4Bd9O92CagFsl10DLjLeNiV8dqbFxzMSfg")//삭제예정
        viewModelScope.launch {
            val apiResponse = PartyRepository.instance.getParties(getPartyRequest)
            if (apiResponse.code()==200){
                val getPartiesResponse: PartyDto.Companion.GetPartiesResponse = apiResponse.body()?: PartyDto.Companion.GetPartiesResponse(
                    arrayListOf()
                )
                if (isMore){
                    val copyList = _partyPreviewList.value?.toMutableList()
                    copyList?.addAll(getPartiesResponse.parties)
                    copyList?.let {
                        _partyPreviewList.value = copyList
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
}