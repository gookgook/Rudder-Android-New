package com.rudder.model.service

import com.rudder.model.dto.LoginInfo
import com.rudder.model.dto.LoginResult
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/auth")
    suspend fun getLoginResult(
        @Body loginInfo: LoginInfo
    ): Response<LoginResult>
}
