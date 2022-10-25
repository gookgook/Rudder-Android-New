package com.rudder.src.host.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AcceptDialogVIewModel: ViewModel() {

    private var _partyId:Int? = null

    fun acceptApplicant(partyId: Int, partyMemberId: Int) {
        viewModelScope.launch {
            //val acceptApplicantRequest =
        }
    }
}