package com.rudder.src.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.model.RetrofitClient
import com.rudder.model.service.GetPartyProfileService
import kotlinx.coroutines.launch

class MyProfileViewModel: ViewModel() {
    val userNickname = MutableLiveData<String>()
    val userDescription = MutableLiveData<String>()

    val getProfileFlag = MutableLiveData<Int>()

    fun getProfile(){
        viewModelScope.launch {
        }
    }
}