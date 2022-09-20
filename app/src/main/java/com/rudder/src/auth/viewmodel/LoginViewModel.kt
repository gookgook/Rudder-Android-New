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

            val loginRequest: Response<LoginResult> = loginService.getLoginResult(LoginInfo("notiToken","ios",safeUserId, safeUserPassword))
            Log.d("logres",loginRequest.body().toString())
            when(loginRequest.code()) {
                200 -> {
                    val accessToken = loginRequest.body()!!.accessToken
                    App.prefs.setValue("authToken",accessToken)
                    RetrofitClient.updateAuthToken(accessToken)
                    loginResultFlag.value = 1
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