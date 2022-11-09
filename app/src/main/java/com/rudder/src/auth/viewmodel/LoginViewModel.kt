package com.rudder.src.auth.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.config.App
import com.rudder.model.RetrofitClient
import com.rudder.model.dto.LoginInfo
import com.rudder.model.dto.LoginResult
import com.rudder.model.service.LoginService
import kotlinx.coroutines.launch
import retrofit2.Response
import kotlin.math.log

class LoginViewModel : ViewModel() {
    val userId = MutableLiveData<String>()
    val userPassword = MutableLiveData<String>()

    val loginResultFlag = MutableLiveData<Int> ()

    val isLoadingFlag = MutableLiveData<Boolean> (false)

    lateinit var loginResultText: String

    fun onClickLogin(){

        val safeUserId: String = userId.value?:run {
            loginResultFlag.postValue(-2)
            return
        }
        val safeUserPassword: String = userPassword.value?:run {
            loginResultFlag.postValue(-2)
            return
        }

        val loginService: LoginService = RetrofitClient.getClient(BuildConfig.BASE_URL).create(LoginService::class.java)

        viewModelScope.launch {

            isLoadingFlag.value = true
            val loginRequest: Response<LoginResult> = loginService.getLoginResult(LoginInfo("notiToken","ios",safeUserId, safeUserPassword))
            isLoadingFlag.value = false

            when(loginRequest.code()) {

                200 -> {
                    val accessToken = loginRequest.body()!!.accessToken
                    val userInfoId = loginRequest.body()!!.userInfoId
                    App.prefs.setValue("authToken",accessToken)
                    App.prefs.setValue("userInfoId",userInfoId.toString())
                    RetrofitClient.updateAuthToken(accessToken)
                    loginResultFlag.value = 1
                }
                401 -> {
                    loginResultFlag.value = 2
                }
                else -> {
                    Log.d("loginResult","login not 200")
                    loginResultFlag.value = -1
                }

            }
            // printResult(loginRequest.body().toString())
        }
    }
}