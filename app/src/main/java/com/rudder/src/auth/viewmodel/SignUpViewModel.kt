package com.rudder.src.auth.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.mvvm.Model.EmailValidateService
import com.rudder.model.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Response

class SignUpViewModel: ViewModel() {
    val userId = MutableLiveData<String>()
    val userPassword = MutableLiveData<String>()
    val userNickname = MutableLiveData<String>()
    val userDescription = MutableLiveData<String>()

    val emailValidateResultFlag = MutableLiveData<Int>()
    val profileCheckFlag = MutableLiveData<Int>()

    fun onClickNext() {
        val safeUserId: String = userId.value?:run { emailValidateResultFlag.postValue(-2); return }
        val safeUserPassword: String = userPassword.value?:run {  emailValidateResultFlag.postValue(-2); return }

        val emailValidateService: EmailValidateService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(EmailValidateService::class.java)

        viewModelScope.launch {

            val emailValidateRequest: Response<Void> = emailValidateService.getEmailValidateResult(safeUserId)
            Log.d("emailVal",emailValidateRequest.code().toString())
            when (emailValidateRequest.code()) {
                200 -> emailValidateResultFlag.value = 1
                406 -> emailValidateResultFlag.value = 2
                409 -> emailValidateResultFlag.value = 3
                else -> emailValidateResultFlag.value = -1
            }
        }
    }

    fun onClickNext2(){
        val safeUserNickname: String = userNickname.value?:run { profileCheckFlag.value = -2; return }
        val safeUserDescription: String = userDescription.value?:run {  profileCheckFlag.value = -2; return }

        if (safeUserDescription.length < 20) { profileCheckFlag.value = -3; return }

        profileCheckFlag.value = 1
    }
}