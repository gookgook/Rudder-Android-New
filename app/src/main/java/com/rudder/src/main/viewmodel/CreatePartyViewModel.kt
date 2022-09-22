package com.rudder.src.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.model.dto.PartyDto
import com.rudder.model.RetrofitClient
import com.rudder.model.repository.PartyRepository
import kotlinx.coroutines.launch
import java.util.*

class CreatePartyViewModel : ViewModel() {

    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String> = _toastMessage



    val partyTitle: MutableLiveData<String> = MutableLiveData()
    val partyDescription: MutableLiveData<String> = MutableLiveData()
    val partyLocation: MutableLiveData<String> = MutableLiveData()

    val partyCalendar: Calendar = Calendar.getInstance()
    var partyMemberCount: Int? = null

    fun postParty() {

    }










}